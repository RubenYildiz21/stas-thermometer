package stas.thermometer.domains;

import java.util.Map;

/**
 * Interface pour lire la configuration liée aux profils de température.
 *
 * <p>Cette interface définit les méthodes nécessaires pour accéder aux informations de configuration
 * qui sont cruciales pour le fonctionnement du thermomètre. Elle permet d'extraire des données
 * telles que le profil de température, les formats de date et de température, ainsi que le nom du thermomètre.</p>
 *
 * <p>Elle est conçue pour être implémentée par des classes qui lisent la configuration à partir de différentes sources,
 * telles que des fichiers de configuration, des bases de données, etc.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public interface ConfigurationReader {

    /**
     * Récupère le profil de température actuel.
     *
     * @return le {@link TemperatureProfile} contenant les jalons de température.
     */
    TemperatureProfile getTemperatureProfile();

    /**
     * Récupère une valeur spécifique à partir d'une section et d'une clé de configuration.
     *
     * @param section la section dans la source de configuration.
     * @param key la clé spécifique dans la section pour laquelle la valeur est requise.
     * @return la valeur correspondant à la section et à la clé spécifiées.
     */
    String getValue(String section, String key);

    /**
     * Récupère toutes les sections disponibles dans la source de configuration.
     *
     * @return une Map contenant les noms des sections et une autre Map avec les paires clé-valeur de chaque section.
     */
    Map<String, Map<String, String>> getAllSections();

    /**
     * Récupère le format des dates utilisé dans les mesures et les logs.
     *
     * @return une chaîne représentant le format des dates, par exemple "dd-MM-yyyy HH:mm:ss".
     */
    String getDateTimeFormat();

    /**
     * Récupère le format utilisé pour les valeurs de température.
     *
     * @return une chaîne représentant le format de température, par exemple "#.##" pour limiter à deux décimales.
     */
    String getTemperatureFormat();

    /**
     * Récupère le nom unique ou descriptif du thermomètre.
     *
     * @return une chaîne représentant le nom du thermomètre.
     */
    String getThermometerName();

}
