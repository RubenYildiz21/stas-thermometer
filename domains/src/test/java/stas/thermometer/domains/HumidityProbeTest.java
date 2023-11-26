package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class HumidityProbeTest {

    private HumidityProbe humidityProbe;

    @Mock
    private Profiles profile;

    @Mock
    private Observer observer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(profile.getExpectedHumidity(any())).thenReturn(50.0);
        humidityProbe = new HumidityProbe(profile);
        humidityProbe.addObserver(observer);
    }

    @Test
    void shouldNotifyObserverWithAdjustedHumidity() {
        // Act
        humidityProbe.measureAndNotify();

        // Assert
        verify(observer, atLeastOnce()).updateHumidity(anyDouble());
    }

    @Test
    void shouldIncreaseHumidityCorrectly() {
        // Arrange
        double initialHumidity = humidityProbe.getAdjustedHumidity();

        // Act
        humidityProbe.increaseHumidity();
        humidityProbe.increaseHumidity();
        humidityProbe.increaseHumidity();
        double increasedHumidity = humidityProbe.getAdjustedHumidity();
        // Assert
        assertTrue(increasedHumidity > initialHumidity, "Increased humidity should be greater than initial humidity");
    }

    @Test
    void shouldDecreaseHumidityCorrectly() {
        // Arrange
        double initialHumidity = humidityProbe.getAdjustedHumidity();

        // Act
        humidityProbe.decreaseHumidity();
        humidityProbe.decreaseHumidity();
        humidityProbe.decreaseHumidity();
        double decreasedHumidity = humidityProbe.getAdjustedHumidity();

        // Assert
        assertTrue(decreasedHumidity < initialHumidity, "Decreased humidity should be less than initial humidity");
    }
}
