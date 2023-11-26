package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementAggregatorTest {

    @Mock
    private ObserverManager mockObserverManager;
    @Mock
    private DatabaseManager<Measurement> mockMeasurementDbManager;
    @Mock
    private DatabaseManager<Humidity> mockHumidityDbManager;
    @Mock
    private Profiles mockProfiles;

    private MeasurementAggregator aggregator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        aggregator = new MeasurementAggregator(mockObserverManager, mockProfiles, "TestThermometer", mockMeasurementDbManager, mockHumidityDbManager);
    }

    @Test
    void shouldAddMeasurementAndNotifyObservers() {
        Measurement measurement = new Measurement("TestThermometer", 25.0, LocalDateTime.now());
        aggregator.addMeasurement(measurement);

        assertTrue(aggregator.hasNewMeasurement());
        verify(mockObserverManager).notifyTemperatureObservers(25.0);
    }

    @Test
    void shouldAddHumidityMeasurementAndNotifyObservers() {
        Humidity humidity = new Humidity("TestThermometer", 55.0, LocalDateTime.now());
        aggregator.addHumidityMeasurement(humidity);

        assertTrue(aggregator.hasNewMeasurement());
        verify(mockObserverManager).notifyHumidityObservers(55.0);
    }

    @Test
    void shouldCalculateAverageMeasurement() {
        aggregator.addMeasurement(new Measurement("TestThermometer", 20.0, LocalDateTime.now()));
        aggregator.addMeasurement(new Measurement("TestThermometer", 30.0, LocalDateTime.now()));

        Measurement averageMeasurement = aggregator.getAverageMeasurement();

        assertNotNull(averageMeasurement);
        assertEquals(25.0, averageMeasurement.temperature());
    }

    @Test
    void shouldCalculateAverageHumidity() {
        aggregator.addHumidityMeasurement(new Humidity("TestThermometer", 40.0, LocalDateTime.now()));
        aggregator.addHumidityMeasurement(new Humidity("TestThermometer", 60.0, LocalDateTime.now()));

        Humidity averageHumidity = aggregator.getAverageHumidity();

        assertNotNull(averageHumidity);
        assertEquals(50.0, averageHumidity.humidity());
    }

    @Test
    void shouldCalculateAndNotifyAverageTemperature() {
        aggregator.addMeasurement(new Measurement("TestThermometer", 22.0, LocalDateTime.now()));
        aggregator.addMeasurement(new Measurement("TestThermometer", 28.0, LocalDateTime.now()));

        aggregator.calculateAndNotifyAverageTemperature();

        verify(mockObserverManager).notifyAverageTemperatureObservers(any(Measurement.class));
    }

    @Test
    void shouldCalculateAndNotifyAverageHumidity() {
        aggregator.addHumidityMeasurement(new Humidity("TestThermometer", 45.0, LocalDateTime.now()));
        aggregator.addHumidityMeasurement(new Humidity("TestThermometer", 55.0, LocalDateTime.now()));

        aggregator.calculateAndNotifyAverageHumidity();

        verify(mockObserverManager).notifyAverageHumidityObservers(any(Humidity.class));
    }

    @Test
    void shouldTriggerTemperatureAlertIfConditionMet() {
        when(mockProfiles.getExpectedTemperature(any(LocalDateTime.class))).thenReturn(25.0);
        Measurement measurement = new Measurement("TestThermometer", 40.0, LocalDateTime.now());
        aggregator.addMeasurement(measurement);

        aggregator.calculateAndNotifyAverageTemperature();

        verify(mockObserverManager).notifyTemperatureAlert(anyString(), anyDouble(), anyDouble());
    }

    @Test
    void shouldTriggerHumidityAlertIfConditionMet() {
        when(mockProfiles.getExpectedHumidity(any(LocalDateTime.class))).thenReturn(50.0);
        Humidity humidity = new Humidity("TestThermometer", 70.0, LocalDateTime.now());
        aggregator.addHumidityMeasurement(humidity);

        aggregator.calculateAndNotifyAverageHumidity();

        verify(mockObserverManager).notifyHumidityAlert(anyString(), anyDouble(), anyDouble());
    }
}
