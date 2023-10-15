package stas.thermometer.presentations;

import stas.thermometer.domains.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

/**
 * Cette classe fait le lien entre la vue et le domaine, gérant la logique de présentation pour un thermomètre.
 * Elle implémente {@code TemperatureObserver} et {@code AverageMeasurementObserver} pour réagir aux changements de température et aux mesures moyennes.
 */
public class ThermometerPresenter implements TemperatureObserver, AverageMeasurementObserver {

    private final ThermometerView view;
    private final MeasurementAggregator aggregator;
    private final ConfigurationInterface config;
    private final TemperatureProbe probe;

    /**
     * Construit un ThermometerPresenter avec la vue, l'agrégateur de mesures, la configuration, et la sonde de température spécifiés.
     */
    public ThermometerPresenter(ThermometerView view, MeasurementAggregator aggregator, ConfigurationInterface config, TemperatureProbe probe) {
        this.view = view;
        this.aggregator = aggregator;
        this.config = config;
        this.probe = probe;
    }

    /**
     * Met à jour la température et ajoute la mesure à l'agrégateur.
     */
    @Override
    public void updateTemperature(double temperature) {
        Measurement measurement = new Measurement(temperature, LocalDateTime.now());
        aggregator.addMeasurement(measurement);
    }

    /**
     * Ajoute un observateur à l'agrégateur.
     */
    @Override
    public void addObserver(TemperatureObserver observer) {
        aggregator.addObserver(observer);
    }


    /**
     * Notifie les observateurs avec la température spécifiée.
     */
    @Override
    public void notifyObservers(double temperature) {
        aggregator.notifyObservers(temperature);
    }

    /**
     * Met à jour et affiche la mesure moyenne.
     */
    @Override
    public void updateAverageMeasurement(Measurement averageMeasurement) {
        String formattedTemperature = Measurement.getFormatTemperature(averageMeasurement.temperature(), ".%2f°C");
        String formattedTimestamp = Measurement.getFormatTimestamp(averageMeasurement.timestamp(), "yyyy-MM-dd HH:mm:ss");
        view.displayTemperature("Temperature moyenne : " + formattedTemperature + " à " + formattedTimestamp);
    }

    /**
     * Affiche le nom du thermomètre.
     */
    @Override
    public void displayThermometerName(){
        String name = config.thermometerName();
        view.displayThermometerName("Thermometer name : " + name);
    }

    /**
     * Exécute la boucle principale de l'application, traitant les commandes entrées par l'utilisateur.
     */
    public void runMainLoop() {
        try (var input = new BufferedReader(new InputStreamReader(System.in))) {
            boolean quitRequested = false;
            do {
                view.display("> ");
                String cmd = input.readLine().strip();
                quitRequested = processCommand(cmd);
            } while (!quitRequested);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Traite la commande entrée par l'utilisateur.
     */
    private boolean processCommand(String cmd) {
        return switch (cmd) {
            case "h" -> {
                displayHelp();
                yield false;
            }
            case "q" -> true;
            case "r" -> {
                handleRaiseTemperature();
                yield false;
            }
            case "m" -> {
                handleMitigateTemperature();
                yield false;
            }
            default -> {
                displayUnknownCommand();
                yield false;
            }
        };
    }

    /**
     * Affiche l'aide pour les commandes.
     */
    private void displayHelp() {
        view.display("l - List the commands");
        view.display("m - Mitigate the generated temperature");
        view.display("r - Raise the generated temperature");
        view.display("q - quit the application");
    }

    /**
     * Gère la demande d'augmentation de la température générée.
     */
    private void handleRaiseTemperature() {
        probe.increaseTemperature();
    }

    /**
     * Gère la demande de diminution de la température générée.
     */
    private void handleMitigateTemperature() {
        probe.decreaseTemperature();
    }

    /**
     * Affiche une commande inconnue.
     */
    private void displayUnknownCommand() {
        view.display("Unknown command. Type 'h' for help.");
    }

    /**
     * Gère et délègue l'affichage des alertes à la vue.
     */
    @Override
    public void alertTriggered(String alertType, double expectedTemperature, double difference) {
        view.displayAlert(alertType, expectedTemperature, difference);
    }
}
