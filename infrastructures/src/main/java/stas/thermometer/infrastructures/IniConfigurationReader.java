package stas.thermometer.infrastructures;

import stas.thermometer.domains.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private ProfileBuilder profileBuilder = null;

    /**
     * Constructeur de IniConfigurationReader.
     *
     * @param inputStream le flux d'entrée contenant les données du fichier INI.
     * @throws FileNotFoundException si le fichier INI n'est pas trouvé.
     */
    public IniConfigurationReader(InputStream inputStream) throws FileNotFoundException {
        parseFile(new Scanner(inputStream));
        if(sections.isEmpty()){
            return;
        }
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
            if (line.isEmpty() || line.startsWith(";")) {
                continue;
            } else if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                sections.put(currentSection, new HashMap<>());
            } else {
                addKeyValuePair(currentSection, line);
            }
        }
        scanner.close();
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
        if (keyValue.length == nbKeyValue ) {
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            Map<String, String> currentKeyValuePairs = sections.get(currentSection);
            if (currentKeyValuePairs != null) {
                currentKeyValuePairs.put(key, value);
            }
        }
    }

    @Override
    public Profiles getProfile() {
        if(profileBuilder == null) {
            profileBuilder = new ProfileBuilder(sections);
        }
        return profileBuilder.buildProfiles();
    }

    /**
     * Récupère une valeur spécifique de la configuration.
     *
     * @param section la section de la configuration.
     * @param key la clé de la configuration.
     * @return la valeur correspondante à la clé, ou null si elle n'existe pas.
     */
    private String getValue(String section, String key){
        Map<String, String> keyValue = sections.get(section);
        return keyValue != null ? keyValue.get(key) : null;
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
        return getValue("format", "value");
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

    @Override
    public String[] getServerDbInfo() {
        return new String[]{
                getValue("bd", "url"),
                getValue("bd", "user"),
                getValue("bd", "password"),
                getValue("bd", "dbName")
        };
    }

}