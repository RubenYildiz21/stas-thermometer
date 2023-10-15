package stas.thermometer.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import stas.thermometer.domains.TemperatureObserver;
import stas.thermometer.domains.TemperatureProbe;
import stas.thermometer.domains.TemperatureProfile;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

class TemperatureProbeTest {

    private TemperatureProbe probe;
    private TemperatureProfile profile;
    private TemperatureObserver observer;

    @BeforeEach
    void setUp() {
        profile = mock(TemperatureProfile.class);
        probe = new TemperatureProbe(profile);
        observer = mock(TemperatureObserver.class);
        probe.addObserver(observer);
    }

    @Test
    void measureAndNotify_ShouldNotifyObserverWithAdjustedTemperature() {
        when(profile.getExpectedTemperature(any(LocalDateTime.class))).thenReturn(25.0);
        probe.measureAndNotify();

        // La température ajustée est aléatoire, vérifie que la méthode a été appelée avec n'importe quelle température.
        verify(observer, times(1)).updateTemperature(anyDouble());
    }

    @Test
    void increaseTemperature_ShouldAffectAdjustedTemperature() {
        probe.increaseTemperature();
        double initialTemperature = probe.getAdjustedTemperature();
        probe.increaseTemperature();

        // Vérifiez que la température ajustée a augmenté.
        assert(probe.getAdjustedTemperature() > initialTemperature);
    }

    @Test
    void checkAndTriggerAlert_ShouldTriggerAlertIfDifferenceIsSignificant() {
        when(profile.getExpectedTemperature(any(LocalDateTime.class))).thenReturn(20.0);

        // Simule une condition de surchauffe suffisante pour déclencher une alerte.
        for (int i = 0; i < 5; i++) { // Augmentez la température de 2.5 degrés, par exemple
            probe.increaseTemperature();
        }
        probe.measureAndNotify();

        // Vérifiez si l'alerte a été déclenchée.
        verify(observer, times(1)).alertTriggered(eq("Surchauffe"), eq(20.0), anyDouble());
    }

}

