package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

class RefreshProbeTaskTest {

    @Mock
    private TemperatureProbe mockTemperatureProbe;
    @Mock
    private HumidityProbe mockHumidityProbe;

    private RefreshProbeTask refreshProbeTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshProbeTask = new RefreshProbeTask(mockTemperatureProbe, mockHumidityProbe);
    }

    @Test
    void shouldTriggerTemperatureMeasurement() {
        refreshProbeTask.run();

        verify(mockTemperatureProbe).measureAndNotify();
    }

    @Test
    void shouldTriggerHumidityMeasurement() {
        refreshProbeTask.run();

        verify(mockHumidityProbe).measureAndNotify();
    }

}

