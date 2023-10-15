package stas.thermometer.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stas.thermometer.domains.AverageMeasurementObserver;
import stas.thermometer.domains.Measurement;
import stas.thermometer.domains.MeasurementAggregator;
import stas.thermometer.domains.TemperatureObserver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

public class MeasurementAggregatorTest {

    private MeasurementAggregator aggregator;
    private AverageMeasurementObserver mockAverageObserver;

    @BeforeEach
    public void setUp() {
        aggregator = new MeasurementAggregator();
        mockAverageObserver = mock(AverageMeasurementObserver.class);
        TemperatureObserver mockTemperatureObserver = mock(TemperatureObserver.class);
        aggregator.addAverageObserver(mockAverageObserver);
        aggregator.addObserver(mockTemperatureObserver);
    }

    @Test
    public void shouldCalculateAverageMeasurementCorrectly() {
        // Arrange
        aggregator.addMeasurement(new Measurement(20.5, LocalDateTime.now()));
        aggregator.addMeasurement(new Measurement(22.5, LocalDateTime.now()));

        // Act
        Measurement result = aggregator.getAverageMeasurement();

        // Assert
        assertNotNull(result);
        assertEquals(21.5, result.temperature(), 0.001);
    }

    @Test
    public void givenNoMeasurements_shouldReturnNullAverageMeasurement() {
        // Arrange
        // (Nothing to do because no measurements are added)

        // Act
        Measurement result = aggregator.getAverageMeasurement();

        // Assert
        assertNull(result);
    }

    @Test
    public void shouldNotifyAverageObservers() {
        // Arrange
        aggregator.addMeasurement(new Measurement(20.5, LocalDateTime.now()));
        aggregator.addMeasurement(new Measurement(22.5, LocalDateTime.now()));

        // Act
        aggregator.calculateAndNotifyAverage();

        // Assert
        verify(mockAverageObserver, times(1)).updateAverageMeasurement(any(Measurement.class));
    }

    @Test
    public void shouldAddTemperatureObserver() {
        // Arrange
        TemperatureObserver newObserver = mock(TemperatureObserver.class);

        // Act
        aggregator.addObserver(newObserver);
        aggregator.notifyObservers(25.0);

        // Assert
        verify(newObserver, times(1)).updateTemperature(25.0);
    }

    @Test
    public void shouldUpdateTemperatureAndAddMeasurement() {
        // Arrange
        double newTemperature = 26.0;

        // Act
        aggregator.updateTemperature(newTemperature);

        // Assert
        Measurement result = aggregator.getAverageMeasurement();
        assertNotNull(result);
        assertEquals(newTemperature, result.temperature(), 0.001);
    }

}
