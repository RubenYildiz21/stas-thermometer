package stas.thermometer.infrastructures;

import stas.thermometer.domains.ConfigurationReader;
import stas.thermometer.domains.TemperatureJalons;
import stas.thermometer.domains.TemperatureProfile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implémentation de {@code ConfigurationReader} pour lire les configurations depuis un fichier INI.
 *
 * <p>Cette classe lit les configurations d'un fichier INI et fournit des méthodes pour récupérer
 * des informations spécifiques telles que les profils de température, les formats de date et de
 * température, et le nom du thermomètre.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public class IniConfigurationReader implements ConfigurationReader {

    private final Map<String, Map<String, String>> sections = new HashMap<>();

    /**
     * Constructeur de IniConfigurationReader.
     *
     * @param inputStream le flux d'entrée contenant les données du fichier INI.
     * @throws FileNotFoundException si le fichier INI n'est pas trouvé.
     */
    public IniConfigurationReader(InputStream inputStream) throws FileNotFoundException {
        parseFile(new Scanner(inputStream));
    }

    /**
     * Analyse le fichier INI et stocke les informations dans la structure de données.
     *
     * @param scanner un Scanner pour lire le fichier INI.
     */
    private void parseFile(Scanner scanner) {
        String currentSection = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (isCommentOrEmpty(line)) {
                continue;
            } else if (isNewSection(line)) {
                currentSection = getSectionName(line);
                sections.put(currentSection, new HashMap<>());
            } else {
                addKeyValuePair(currentSection, line);
            }
        }
        scanner.close();
    }

    /**
     * Détermine si une ligne donnée est un commentaire ou une ligne vide.
     *
     * @param line la ligne à vérifier.
     * @return true si la ligne est un commentaire ou vide, false sinon.
     */
    private boolean isCommentOrEmpty(String line) {
        return line.isEmpty() || line.startsWith(";");
    }

    /**
     * Vérifie si la ligne spécifiée débute une nouvelle section dans le fichier INI.
     *
     * @param line la ligne à vérifier.
     * @return true si la ligne marque le début d'une nouvelle section, false sinon.
     */
    private boolean isNewSection(String line) {
        return line.startsWith("[") && line.endsWith("]");
    }

    /**
     * Extrait le nom de la section d'une ligne donnée.
     *
     * @param line la ligne contenant la section.
     * @return le nom de la section.
     */
    private String getSectionName(String line) {
        return line.substring(1, line.length() - 1);
    }

    /**
     * Ajoute une paire clé-valeur à la section actuelle.
     *
     * @param currentSection le nom de la section actuelle.
     * @param line la ligne contenant la paire clé-valeur.
     */
    private void addKeyValuePair(String currentSection, String line) {
        String[] keyValue = line.split("=", 2);
        int nbKeyValue = 2;
        if (keyValue.length == nbKeyValue) {
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            Map<String, String> currentKeyValuePairs = sections.get(currentSection);
            if (currentKeyValuePairs != null) {
                currentKeyValuePairs.put(key, value);
            }
        }
    }

    /**
     * Récupère le profil de température basé sur les configurations lues du fichier INI.
     *
     * @return un objet {@code TemperatureProfile} contenant les jalons de température.
     */
    @Override
    public TemperatureProfile getTemperatureProfile() {
        Map<String, String> profileSection = sections.get("profile");
        List<TemperatureJalons> jalons = new ArrayList<>();

        Map<Integer, String> indexKeyMap = getIndexKeyMap(profileSection);
        List<Integer> sortedIndices = getSortedIndices(indexKeyMap);

        // Calculer l'intervalle de temps entre chaque jalon
        int numberOfJalons = profileSection.size();
        int hoursPerJalon = 24 / numberOfJalons; // Ceci suppose une répartition uniforme des jalons sur une période de 24 heures

        for (Integer index : sortedIndices) {
            String key = indexKeyMap.get(index);
            TemperatureJalons jalon = createJalonFromIndexAndKey(index, key, profileSection, hoursPerJalon);
            jalons.add(jalon);
        }

        return new TemperatureProfile(jalons);
    }

    /**
     * Crée un objet {@code TemperatureJalons} à partir d'un index et d'une clé donnés.
     *
     * @param index l'index du jalon.
     * @param key la clé correspondant à la température du jalon.
     * @param profileSection la section du profil contenant les jalons.
     * @param hoursPerJalon le nombre d'heures par jalon.
     * @return un objet {@code TemperatureJalons}.
     */
    private TemperatureJalons createJalonFromIndexAndKey(Integer index, String key, Map<String, String> profileSection, int hoursPerJalon) {
        // Calculer l'heure du jalon basée sur son index
        int hourOfJalon = (index * hoursPerJalon) % 24;
        // Récupérer la température du jalon
        double temperature = Double.parseDouble(profileSection.get(key));
        // Construire l'objet LocalDateTime pour le jalon
        LocalDateTime timeOfJalon = LocalDateTime.now().withHour(hourOfJalon).withMinute(0).withSecond(0).withNano(0);
        // Retourner le nouveau jalon
        return new TemperatureJalons(timeOfJalon, temperature);
    }

    /**
     * Récupère une liste d'indices triés à partir d'une Map d'indices et de clés.
     *
     * @param indexKeyMap la Map contenant les indices et les clés.
     * @return une liste d'indices triés.
     */
    private List<Integer> getSortedIndices(Map<Integer, String> indexKeyMap) {
        List<Integer> sortedIndices = new ArrayList<>(indexKeyMap.keySet());
        Collections.sort(sortedIndices);
        return sortedIndices;
    }

    /**
     * Crée une Map d'indices et de clés à partir d'une section de profil.
     *
     * @param profileSection la section du profil.
     * @return une Map d'indices et de clés.
     */
    private Map<Integer, String> getIndexKeyMap(Map<String, String> profileSection) {
        Map<Integer, String> indexKeyMap = new HashMap<>();
        for (String key : profileSection.keySet()) {
            String indexString = key.replaceAll("[^0-9]", ""); // enlève tous les caractères non numériques
            try {
                int index = Integer.parseInt(indexString);
                indexKeyMap.put(index, key);
            } catch (NumberFormatException e) {
                // Ignorer les clés qui ne contiennent pas un indice valide
                e.printStackTrace();
            }
        }
        return indexKeyMap;
    }


    /**
     * Récupère une valeur spécifique de la configuration.
     *
     * @param section la section de la configuration.
     * @param key la clé de la configuration.
     * @return la valeur correspondante à la clé, ou null si elle n'existe pas.
     */
    @Override
    public String getValue(String section, String key){
        Map<String, String> keyValue = sections.get(section);
        return keyValue != null ? keyValue.get(key) : null;
    }

    /**
     * Récupère toutes les sections de la configuration.
     *
     * @return une Map de toutes les sections, chacune étant une Map de clés et valeurs.
     */
    @Override
    public Map<String, Map<String, String>> getAllSections(){
        return sections;
    }

    /**
     * Récupère le format de date et d'heure de la configuration.
     *
     * @return la chaîne de caractères du format de date et d'heure.
     */
    @Override
    public String getDateTimeFormat(){
        return getValue("format", "datetime");
    }

    /**
     * Récupère le format de température de la configuration.
     *
     * @return la chaîne de caractères du format de température.
     */
    @Override
    public String getTemperatureFormat(){
        return getValue("format", "temperature");
    }

    /**
     * Récupère le nom du thermomètre de la configuration.
     *
     * @return le nom du thermomètre.
     */
    @Override
    public String getThermometerName(){
        return getValue("general", "name");
    }

}
