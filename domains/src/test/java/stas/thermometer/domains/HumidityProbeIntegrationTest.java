package stas.thermometer.domains;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class HumidityProbeIntegrationTest {

    @Test
    void shouldProperlyNotifyAllObservers() {
        // Arrange
        Profiles profile = mock(Profiles.class);
        when(profile.getExpectedHumidity(any())).thenReturn(50.0);
        HumidityProbe humidityProbe = new HumidityProbe(profile);
        Observer observer1 = mock(Observer.class);
        Observer observer2 = mock(Observer.class);
        humidityProbe.addObserver(observer1);
        humidityProbe.addObserver(observer2);

        // Act
        humidityProbe.measureAndNotify();

        // Assert
        verify(observer1, atLeastOnce()).updateHumidity(anyDouble());
        verify(observer2, atLeastOnce()).updateHumidity(anyDouble());
    }
}

