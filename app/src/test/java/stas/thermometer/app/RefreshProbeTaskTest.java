package stas.thermometer.app;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import stas.thermometer.domains.RefreshProbeTask;
import stas.thermometer.domains.TemperatureProbe;

import static org.mockito.Mockito.verify;

class RefreshProbeTaskTest {

    @Test
    void run_ShouldTriggerMeasureAndNotifyOnProbe() {
        // Arrange
        TemperatureProbe probe = Mockito.mock(TemperatureProbe.class);
        RefreshProbeTask task = new RefreshProbeTask(probe);

        // Act
        task.run();

        // Assert
        verify(probe, Mockito.times(1)).measureAndNotify();
    }
}

