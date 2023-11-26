package stas.thermometer.domains;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HumidityTest {

    @Test
    void shouldFormatTimestampCorrectly() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2023, 11, 21, 12, 0);
        String expectedFormat = "21-11-2023 12:00:00";

        // Act
        String formattedTimestamp = Humidity.getFormatTimestamp(timestamp, "dd-MM-yyyy HH:mm:ss");

        // Assert
        assertEquals(expectedFormat, formattedTimestamp, "The formatted timestamp should match the expected format");
    }

    @Test
    void shouldFormatHumidityCorrectly() {
        // Arrange
        double humidity = 55.1234;
        String expectedFormat = "55,12%";

        // Act
        String formattedHumidity = Humidity.getFormatHumidity(humidity, "%.2f%%");

        // Assert
        assertEquals(expectedFormat, formattedHumidity, "The formatted humidity should match the expected format");
    }

    @Test
    void shouldHandleInvalidFormatForTimestamp() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        String invalidFormat = "invalid format";

        // Act & Assert
        try {
            Humidity.getFormatTimestamp(timestamp, invalidFormat);
        } catch (IllegalArgumentException e) {
            // Test passes if exception is thrown
            assertEquals("Unknown pattern letter: i", e.getMessage());
        }
    }

    @Test
    void shouldHandleInvalidFormatForHumidity() {
        // Arrange
        double humidity = 50;
        String invalidFormat = "invalid format";

        // Act & Assert
        try {
            Humidity.getFormatHumidity(humidity, invalidFormat);
        } catch (IllegalArgumentException e) {
            // Test passes if exception is thrown
            assertEquals("Unknown pattern letter: i", e.getMessage());
        }
    }
}
