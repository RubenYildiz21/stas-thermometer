package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureProbeTest {

    @Mock
    private Profiles mockProfile;

    private TemperatureProbe temperatureProbe;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        temperatureProbe = new TemperatureProbe(mockProfile);
    }

    @Test
    void shouldNotifyAllObservers() {
        Observer observer1 = mock(Observer.class);
        Observer observer2 = mock(Observer.class);
        temperatureProbe.addObserver(observer1);
        temperatureProbe.addObserver(observer2);

        temperatureProbe.notifyObservers(25.0);

        verify(observer1).updateTemperature(25.0);
        verify(observer2).updateTemperature(25.0);
    }

    @Test
    void shouldMeasureAndNotifyObservers() {
        when(mockProfile.getExpectedTemperature(any(LocalDateTime.class))).thenReturn(20.0);
        Observer observer = mock(Observer.class);
        temperatureProbe.addObserver(observer);

        temperatureProbe.measureAndNotify();

        verify(observer).updateTemperature(anyDouble());
    }

    @Test
    void shouldReturnAdjustedTemperature() {
        when(mockProfile.getExpectedTemperature(any(LocalDateTime.class))).thenReturn(20.0);
        double adjustedTemperature = temperatureProbe.getAdjustedTemperature();

        assertTrue(adjustedTemperature >= 19.5 && adjustedTemperature <= 20.5);
    }

    @Test
    void shouldIncreaseTemperatureOffset() {
        double initialTemperature = temperatureProbe.getAdjustedTemperature();
        temperatureProbe.increaseTemperature();
        temperatureProbe.increaseTemperature();
        temperatureProbe.increaseTemperature();
        double increasedTemperature = temperatureProbe.getAdjustedTemperature();
        assertTrue(increasedTemperature > initialTemperature, "Increased temperature should be greater than initial temperature");
    }

    @Test
    void shouldDecreaseTemperatureOffset() {
        double initialTemperature = temperatureProbe.getAdjustedTemperature();
        temperatureProbe.decreaseTemperature();
        temperatureProbe.decreaseTemperature();
        temperatureProbe.decreaseTemperature();
        double decreasedTemperature = temperatureProbe.getAdjustedTemperature();
        assertTrue(decreasedTemperature < initialTemperature, "Increased temperature should be greater than initial temperature");
    }

}
