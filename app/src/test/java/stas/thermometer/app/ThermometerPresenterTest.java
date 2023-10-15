package stas.thermometer.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import stas.thermometer.domains.*;
import stas.thermometer.presentations.ThermometerPresenter;
import stas.thermometer.presentations.ThermometerView;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class ThermometerPresenterTest {

    private ThermometerView view;
    private MeasurementAggregator aggregator;

    private ConfigurationInterface config;
    private TemperatureProbe probe;
    private ThermometerPresenter presenter;

    @BeforeEach
    public void setUp() {
        view = mock(ThermometerView.class);
        aggregator = mock(MeasurementAggregator.class);
        config = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "Test Thermometer");  // Créer une vraie instance ici
        probe = mock(TemperatureProbe.class);
        presenter = new ThermometerPresenter(view, aggregator, config, probe);
    }


    @Test
    public void testUpdateAverageMeasurement() {
        // Given
        double temperature = 25.0;
        LocalDateTime now = LocalDateTime.of(2023, 10, 4, 12, 0);
        Measurement averageMeasurement = new Measurement(temperature, now);
        String formattedTemperature = Measurement.getFormatTemperature(temperature, "%.2f°C");
        String formattedTimestamp = Measurement.getFormatTimestamp(now, "yyyy-MM-dd HH:mm:ss");

        // When
        presenter.updateAverageMeasurement(averageMeasurement);

        // Then
        verify(view, times(1)).displayTemperature("Temperature moyenne : " + formattedTemperature + " à " + formattedTimestamp);
    }

    @Test
    public void testUpdateTemperature() {
        // Given
        double temperature = 25.0;

        // When
        presenter.updateTemperature(temperature);

        // Then
        verify(aggregator, times(1)).addMeasurement(argThat(measurement -> measurement.temperature() == temperature));
    }


}
