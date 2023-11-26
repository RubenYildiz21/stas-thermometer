package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class ObserverManagerTest {

    @Mock
    private Observer mockObserver;
    @Mock
    private AverageMeasurementObserver mockAverageObserver;

    private ObserverManager observerManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        observerManager = new ObserverManager();
    }

    @Test
    void shouldAddAndNotifyTemperatureObservers() {
        observerManager.addTemperatureObserver(mockObserver);
        observerManager.notifyTemperatureObservers(25.0);

        verify(mockObserver).updateTemperature(25.0);
    }

    @Test
    void shouldAddAndNotifyTemperatureAlertObservers() {
        observerManager.addAlertObserver(mockObserver);
        observerManager.notifyTemperatureAlert("High", 30.0, 5.0);

        verify(mockObserver).alertTriggeredTemperature("High", 30.0, 5.0);
    }

    @Test
    void shouldAddAndNotifyAverageTemperatureObservers() {
        Measurement averageMeasurement = new Measurement("TestThermometer", 25.0, LocalDateTime.now());
        observerManager.addAverageObserver(mockAverageObserver);
        observerManager.notifyAverageTemperatureObservers(averageMeasurement);

        verify(mockAverageObserver).updateAverageMeasurement(averageMeasurement);
    }

    @Test
    void shouldAddAndNotifyHumidityObservers() {
        observerManager.addTemperatureObserver(mockObserver);
        double humidity = 50.0;
        observerManager.notifyHumidityObservers(humidity);

        verify(mockObserver).updateHumidity(humidity);
    }

    @Test
    void shouldAddAndNotifyAverageHumidityObservers() {
        Humidity averageHumidity = new Humidity("TestThermometer", 25.0, LocalDateTime.now());
        observerManager.addAverageObserver(mockAverageObserver);
        observerManager.notifyAverageHumidityObservers(averageHumidity);

        verify(mockAverageObserver).updateAverageHumidity(averageHumidity);
    }

    @Test
    void shouldAddAndNotifyHumidityAlertObservers() {
        observerManager.addAlertObserver(mockObserver);
        String alertType = "trop humide";
        double expectedHumidity = 40.0;
        double difference = 10.0;

        observerManager.notifyHumidityAlert(alertType, expectedHumidity, difference);

        verify(mockObserver).alertTriggeredHumidity(alertType, expectedHumidity, difference);
    }


}
