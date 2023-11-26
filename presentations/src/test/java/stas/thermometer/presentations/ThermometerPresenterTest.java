package stas.thermometer.presentations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import stas.thermometer.domains.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ThermometerPresenterTest {

    @Mock
    private ThermometerView mockView;
    @Mock
    private MeasurementAggregator mockAggregator;
    @Mock
    private Configuration mockConfig;
    @Mock
    private TemperatureProbe mockTemperatureProbe;
    @Mock
    private HumidityProbe mockHumidityProbe;

    private ThermometerPresenter presenter;

    @BeforeEach
    void setUp() {
        mockView = mock(ThermometerView.class);
        mockAggregator = mock(MeasurementAggregator.class);
        mockConfig = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "Test Thermometer");  // Créer une vraie instance ici
        mockTemperatureProbe = mock(TemperatureProbe.class);
        mockHumidityProbe = mock(HumidityProbe.class);
        presenter = new ThermometerPresenter();
        presenter.setView(mockView);
        presenter.setAggregator(mockAggregator);
        presenter.setConfiguration(mockConfig);
        presenter.setProbes(mockTemperatureProbe, mockHumidityProbe);
    }

    @Test
    void shouldAddTemperatureMeasurementToAggregator() {
        presenter.updateTemperature(25.0);
        verify(mockAggregator).addMeasurement(any(Measurement.class));
    }

    @Test
    void shouldAddHumidityMeasurementToAggregator() {
        presenter.updateHumidity(60.0);
        verify(mockAggregator).addHumidityMeasurement(any(Humidity.class));
    }

    @Test
    void shouldProcessUserCommands() throws IOException {
        // Simuler l'entrée utilisateur
        BufferedReader mockInput = mock(BufferedReader.class);
        when(mockInput.readLine()).thenReturn("h", "q", "");

        // Exécutez la boucle principale
        presenter.runMainLoop();

        // Vérifiez les interactions avec la vue
        verify(mockView, atLeastOnce()).display(anyString());
    }

    @Test
    void shouldDisplayTemperatureAlert() {
        presenter.alertTriggeredTemperature("High", 25.0, 1.0);
        verify(mockView).displayAlertTemperature("High", 25.0, 1.0);
    }

    @Test
    void shouldDisplayHumidityAlert() {
        presenter.alertTriggeredHumidity("Low", 40.0, 5.0);
        verify(mockView).displayAlertHumidity("Low", 40.0, 5.0);
    }


    @Test
    void shouldSwitchProbe() {
        // L'état initial est la sonde de température
        presenter.switchProbe();
        // Maintenant, il devrait être sur la sonde d'humidité
        verify(mockView).display("Current probe : Humidity");

        presenter.switchProbe();
        // Revenir à la sonde de température
        verify(mockView).display("Current probe : Temperature");
    }

    @Test
    void souldTestUpdateAverageMeasurement() {
        // Arrange
        ThermometerPresenter presenter = new ThermometerPresenter();
        ThermometerView mockView = mock(ThermometerView.class);
        presenter.setView(mockView);
        Measurement mockMeasurement = new Measurement("Thermometer1", 25.0, LocalDateTime.now());

        // Act
        presenter.updateAverageMeasurement(mockMeasurement);

        // Assert
        verify(mockView).displayTemperature(anyString()); // Vérifie que displayTemperature est appelé avec n'importe quelle chaîne
    }

    @Test
    void testUpdateAverageHumidity() {
        // Arrange
        ThermometerPresenter presenter = new ThermometerPresenter();
        ThermometerView mockView = mock(ThermometerView.class);
        presenter.setView(mockView);
        Humidity mockHumidity = new Humidity("Thermometer1", 0.25, LocalDateTime.now());

        // Act
        presenter.updateAverageHumidity(mockHumidity);

        // Assert
        verify(mockView).displayHumidity(anyString()); // Vérifie que displayTemperature est appelé avec n'importe quelle chaîne
    }

    @Test
    void testDisplayHelp() {
        // Arrange
        ThermometerPresenter presenter = new ThermometerPresenter();
        ThermometerView mockView = mock(ThermometerView.class);
        presenter.setView(mockView);

        // Act
        presenter.displayHelp();

        // Assert
        verify(mockView).display(contains("List the commands")); // Vérifie que le texte correct est affiché
    }

    @Test
    void testDisplayUnknownCommand() {
        // Arrange
        ThermometerPresenter presenter = new ThermometerPresenter();
        ThermometerView mockView = mock(ThermometerView.class);
        presenter.setView(mockView);

        // Act
        presenter.displayUnknownCommand();

        // Assert
        verify(mockView).display(contains("Unknown command"));
    }

    @Test
    void testAddObserver() {
        // Arrange
        ThermometerPresenter presenter = new ThermometerPresenter();
        MeasurementAggregator mockAggregator = mock(MeasurementAggregator.class);
        presenter.setAggregator(mockAggregator);
        Observer mockObserver = mock(Observer.class);

        // Act
        presenter.addObserver(mockObserver);

        // Assert
        verify(mockAggregator).addObserver(mockObserver);
    }

    @Test
    void testProcessCommand() {
        // Test pour la commande 'h' (aide)
        assertFalse(presenter.processCommand("h"));
        verify(mockView, times(5)).display(anyString());

        // Test pour la commande 'q' (quitter)
        assertTrue(presenter.processCommand("q"));

        // Test pour la commande 'r' (augmenter la valeur)
        assertFalse(presenter.processCommand("r"));
        // Ici, vous pouvez ajouter des vérifications supplémentaires si nécessaire

        // Test pour la commande 'm' (diminuer la valeur)
        assertFalse(presenter.processCommand("m"));
        // Ajoutez des vérifications supplémentaires si nécessaire

        // Test pour la commande 's' (changer de sonde)
        assertFalse(presenter.processCommand("s"));
        verify(mockView, atLeastOnce()).display(contains("Current probe")); // Vérifie que la vue affiche le changement de sonde

        // Test pour une commande inconnue
        assertFalse(presenter.processCommand("xyz"));
        verify(mockView).display(contains("Unknown command"));
    }

    @Test
    void testNotifyHumidityObservers() {
        // Arrange
        presenter.setAggregator(mockAggregator);

        // Act
        presenter.notifyHumidityObservers(50.0);

        // Assert
        verify(mockAggregator).notifyHumidityObservers(50.0);
    }
    @Test
    void testNotifyObservers() {
        // Arrange
        presenter.setAggregator(mockAggregator);

        // Act
        presenter.notifyObservers(25.0);

        // Assert
        verify(mockAggregator).notifyObservers(25.0);
    }

    @Test
    void testAddAlertObserver() {
        // Arrange
        Observer mockObserver = mock(Observer.class);
        presenter.setAggregator(mockAggregator);

        // Act
        presenter.addAlertObserver(mockObserver);

        // Assert
        verify(mockAggregator).addAlertObserver(mockObserver);
    }
}
