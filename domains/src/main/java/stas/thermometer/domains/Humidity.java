package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
public record Humidity(String thermometerName, double humidity, LocalDateTime timestamp) {

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
     * @param humidity la valeur de température à formater.
     * @param format le format de température sous forme de chaîne de caractères (par exemple, "%.2f°C" pour une température en Celsius avec deux décimales).
     * @return la chaîne de caractères représentant la température formatée.
     */
    public static String getFormatHumidity(double humidity, String format) {
        String javaFormat = format.replace(format, "%.2f%%");
        return String.format(javaFormat, humidity);
    }
}
