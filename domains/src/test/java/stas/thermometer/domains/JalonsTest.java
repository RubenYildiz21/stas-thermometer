package stas.thermometer.domains;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class JalonsTest {

    @Test
    void shouldCorrectlyAssignTimeAndValue() {
        // Arrange
        LocalDateTime testTime = LocalDateTime.now();
        double testValue = 25.5;

        // Act
        Jalons jalon = new Jalons(testTime, testValue);

        // Assert
        assertEquals(testTime, jalon.time(), "Time should be correctly assigned");
        assertEquals(testValue, jalon.value(), "Value should be correctly assigned");
    }

    @Test
    void shouldRemainImmutable() {
        // Arrange
        LocalDateTime initialTime = LocalDateTime.now();
        double initialValue = 22.0;
        Jalons jalon = new Jalons(initialTime, initialValue);

        // Act
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        double newValue = 24.0;

        Jalons newJalon = new Jalons(newTime, newValue);

        // Assert
        assertNotEquals(newJalon.time(), jalon.time(), "Time should not change in the original jalon");
        assertNotEquals(newJalon.value(), jalon.value(), "Value should not change in the original jalon");
    }
}

