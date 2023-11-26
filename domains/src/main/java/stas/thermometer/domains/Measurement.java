package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Représente une mesure de température à un moment donné.
 *
 * <p>Cette classe enregistre une valeur de température et le moment exact où elle a été enregistrée.
 * Elle fournit également des méthodes pour formater la température et l'horodatage pour l'affichage ou le traitement ultérieur.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public record Measurement(String thermometerName, double temperature, LocalDateTime timestamp) {

    /**
     * Formate un objet LocalDateTime en chaîne de caractères selon un format spécifié.
     *
     * @param timestamp l'objet LocalDateTime à formater.
     * @param format le format de date/heure sous forme de chaîne de caractères (par exemple, "dd-MM-yyyy HH:mm:ss").
     * @return la chaîne de caractères représentant l'horodatage formaté.
     */
    public static String getFormatTimestamp(LocalDateTime timestamp, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.FRANCE);
        return timestamp.format(formatter);
    }

    /**
     * Formate une valeur de température selon un format spécifié.
     *
     * @param temperature la valeur de température à formater.
     * @param format le format de température sous forme de chaîne de caractères (par exemple, "%.2f°C" pour une température en Celsius avec deux décimales).
     * @return la chaîne de caractères représentant la température formatée.
     */
    public static String getFormatTemperature(double temperature, String format) {
        String javaFormat = format.replace(format, "%.2f°C");
        return String.format(javaFormat, temperature);
    }

}
