package stas.thermometer.domains;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MeasurementTest {

    @Test
    void shouldCorrectlyAssignValues() {
        // Arrange
        String thermometerName = "ThermoTest";
        double temperature = 23.5;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        Measurement measurement = new Measurement(thermometerName, temperature, timestamp);

        // Assert
        assertEquals(thermometerName, measurement.thermometerName(), "Thermometer name should be correctly assigned");
        assertEquals(temperature, measurement.temperature(), "Temperature should be correctly assigned");
        assertEquals(timestamp, measurement.timestamp(), "Timestamp should be correctly assigned");
    }

    @Test
    void shouldFormatTimestampCorrectly() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2023, 11, 21, 15, 30);
        String expectedFormat = "21-11-2023 15:30:00";

        // Act
        String formattedTimestamp = Measurement.getFormatTimestamp(timestamp, "dd-MM-yyyy HH:mm:ss");

        // Assert
        assertEquals(expectedFormat, formattedTimestamp, "Formatted timestamp should match expected format");
    }

    @Test
    void shouldFormatTemperatureCorrectly() {
        // Arrange
        double temperature = 22.345;
        String expectedFormat = "22,35°C";

        // Act
        String formattedTemperature = Measurement.getFormatTemperature(temperature, "%.2f°C");

        // Assert
        assertEquals(expectedFormat, formattedTemperature, "Formatted temperature should match expected format");
    }
}

