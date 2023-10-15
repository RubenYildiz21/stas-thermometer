package stas.thermometer.app;

import org.junit.jupiter.api.Test;
import stas.thermometer.domains.Measurement;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class MeasurementTest {

    @Test
    public void shouldFormatTemperatureCorrectly() {
        double temperature = 20.5;
        String format = "%.2f°C";
        String expected = "20,50°C";

        String result = Measurement.getFormatTemperature(temperature, format);

        assertEquals(expected, result);
    }

    @Test
    public void shouldFormatTimestampCorrectly() {
        // Supposons que nous ayons une méthode statique pour obtenir un LocalDateTime fixe pour le test
        LocalDateTime timestamp = LocalDateTime.of(2023, 10, 4, 15, 30, 0);
        String format = "yyyy-MM-dd HH:mm:ss";
        String expected = "2023-10-04 15:30:00";  // Supposons que c'est la date/heure fixe

        String result = Measurement.getFormatTimestamp(timestamp, format);

        assertEquals(expected, result);
    }
}
