package stas.thermometer.domains;

/**
 * Représente la configuration d'un thermomètre, incluant les formats pour les dates
 * et les températures, ainsi que le nom du thermomètre.
 *
 * <p>Le record {@code Configuration} contient des informations essentielles utilisées
 * dans le traitement des mesures de température. Il définit les formats standard
 * pour l'affichage des dates et des températures, ainsi que le nom distinctif
 * du thermomètre pour l'identification.</p>
 *
 * @param dateTimeFormat le format des dates utilisé dans les mesures et les logs.
 *                       Par exemple, "dd-MM-yyyy HH:mm:ss".
 * @param temperatureFormat le format utilisé pour les valeurs de température.
 *                          Par exemple, "#.##" pour limiter à deux décimales.
 * @param thermometerName le nom unique ou descriptif du thermomètre.
 * @author Ruben Yildiz
 * @version 1.0
 */
public record Configuration(String dateTimeFormat, String temperatureFormat, String thermometerName) implements ConfigurationInterface {

}
