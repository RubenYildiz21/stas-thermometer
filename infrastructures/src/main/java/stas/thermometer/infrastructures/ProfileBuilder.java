package stas.thermometer.infrastructures;

import stas.thermometer.domains.Jalons;
import stas.thermometer.domains.Profiles;
import java.time.LocalDateTime;
import java.util.*;

public class ProfileBuilder {

    private final Map<String, String> profileSection;
    private final Map<String, String> humiditySection;

    public ProfileBuilder(Map<String, Map<String, String>> sections) {
        this.profileSection = new HashMap<>(sections.get("profile"));
        this.humiditySection = new HashMap<>(sections.get("humidity"));
    }

    public Profiles buildProfiles() {
        List<Jalons> jalonsTemperature = transformProfileSectionToJalons(this.profileSection);
        List<Jalons> jalonsHumidity = transformProfileSectionToJalons(this.humiditySection);
        return new Profiles(jalonsTemperature, jalonsHumidity);
    }

    private List<Jalons> transformProfileSectionToJalons(Map<String, String> profileSection) {
        if (profileSection == null || profileSection.isEmpty()) {
        throw new IllegalStateException("La section du profil est vide ou non définie.");
        }

        Map<Integer, String> indexKeyMap = getIndexKeyMap(profileSection);
        List<Integer> sortedIndices = getSortedIndices(indexKeyMap);

        int numberOfJalons = profileSection.size();
        int hoursPerJalon = 24 / numberOfJalons; // Suppose une répartition uniforme des jalons sur 24 heures

        List<Jalons> jalons = new ArrayList<>();
            for (Integer index : sortedIndices) {
            String key = indexKeyMap.get(index);
            Jalons jalon = createTemperatureJalonFromIndexAndKey(index, key, profileSection, hoursPerJalon);
            jalons.add(jalon);
        }

        return jalons;
}


    /**
     * Crée un objet {@code Jalons} à partir d'un index et d'une clé donnés.
     *
     * @param index l'index du jalon.
     * @param key la clé correspondant à la température du jalon.
     * @param profileSection la section du profil contenant les jalons.
     * @param hoursPerJalon le nombre d'heures par jalon.
     * @return un objet {@code Jalons}.
     */
    private Jalons createTemperatureJalonFromIndexAndKey(Integer index, String key, Map<String, String> profileSection, int hoursPerJalon) {
        // Calculer l'heure du jalon basée sur son index
        int hourOfJalon = (index * hoursPerJalon) % 24;
        // Récupérer la température du jalon
        double temperature = parseTemperatureOrHumidity(profileSection.get(key));
        // Construire l'objet LocalDateTime pour le jalon
        LocalDateTime timeOfJalon = LocalDateTime.now()
                .withHour(hourOfJalon)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        // Retourner le nouveau jalon
        return new Jalons(timeOfJalon, temperature);
    }

    private double parseTemperatureOrHumidity(String temperatureHumidityString) {
        try {
            return Double.parseDouble(temperatureHumidityString);
        } catch (NumberFormatException e) {
            // Gérer l'exception, par exemple en retournant une valeur par défaut ou en lançant une nouvelle exception
            throw new IllegalArgumentException("Format de température ou humidité invalide: " + temperatureHumidityString, e);
        }
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
}


