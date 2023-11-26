package stas.thermometer.presentations;

import stas.thermometer.domains.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

/**
 * Cette classe fait le lien entre la vue et le domaine, gérant la logique de présentation pour un thermomètre.
 * Elle implémente {@code Observer} et {@code AverageMeasurementObserver} pour réagir aux changements de température et aux mesures moyennes.
 */
public class ThermometerPresenter implements Observer, AverageMeasurementObserver {

    private ThermometerView view;
    private MeasurementAggregator aggregator;
    private Configuration config;
    private ProbeState probeState;

    private class ProbeState{
        private final TemperatureProbe probe;
        private final HumidityProbe humidityProbe;
        private boolean isTemperatureProbeSelected;

        private ProbeState(TemperatureProbe probe, HumidityProbe humidityProbe) {
            this.probe = probe;
            this.humidityProbe = humidityProbe;
            this.isTemperatureProbeSelected = true;
        }

        public void switchProbe(){
            isTemperatureProbeSelected = !isTemperatureProbeSelected;
            String probName = isTemperatureProbeSelected ? "Temperature" : "Humidity";
            view.display("Current probe : " + probName);
        }

        private void increaseProbeValue(){
            if(isTemperatureProbeSelected){
                probe.increaseTemperature();
            }else {
                humidityProbe.increaseHumidity();
            }
        }

        private void decreaseProbeValue(){
            if(isTemperatureProbeSelected){
                probe.decreaseTemperature();
            }else {
                humidityProbe.decreaseHumidity();
            }
        }
    }

    // Méthodes 'set' pour chaque dépendance
    public void setView(ThermometerView view) {
        this.view = view;
    }

    public void setAggregator(MeasurementAggregator aggregator) {
        this.aggregator = aggregator;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public void setProbes(TemperatureProbe probe, HumidityProbe humidityProbe) {
        this.probeState = new ProbeState(probe, humidityProbe);
    }
    /**
     * Met à jour la température et ajoute la mesure à l'agrégateur.
     */
    @Override
    public void updateTemperature(double temperature) {
        Measurement measurement = new Measurement(config.thermometerName(), temperature, LocalDateTime.now());
        aggregator.addMeasurement(measurement);
    }

    @Override
    public void updateHumidity(double humidity) {
        Humidity measurement = new Humidity(config.thermometerName(), humidity, LocalDateTime.now());
        aggregator.addHumidityMeasurement(measurement);
    }


    /**
     * Ajoute un observateur à l'agrégateur.
     */
    @Override
    public void addObserver(Observer observer) {
        aggregator.addObserver(observer);
    }

    @Override
    public void addAlertObserver(Observer observer) {
        aggregator.addAlertObserver(observer);
    }


    /**
     * Notifie les observateurs avec la température spécifiée.
     */
    @Override
    public void notifyObservers(double temperature) {
        aggregator.notifyObservers(temperature);
    }

    /**
     * Notifie les observateurs avec la température spécifiée.
     */
    @Override
    public void notifyHumidityObservers(double humidity) {
        aggregator.notifyHumidityObservers(humidity);
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

    @Override
    public void updateAverageHumidity(Humidity averageHumidity) {
        String formattedHumidity = Humidity.getFormatHumidity(averageHumidity.humidity()*100, ".%2f%%");
        String formattedTimestamp = Humidity.getFormatTimestamp(averageHumidity.timestamp(), "yyyy-MM-dd HH:mm:ss");
        view.displayHumidity("Humidité moyenne actuelle : " + formattedHumidity + " à " + formattedTimestamp);
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
                String cmd = input.readLine();
                if(cmd != null){
                    cmd.strip();
                    quitRequested = processCommand(cmd);
                }else {
                    quitRequested = true;
                }
            } while (!quitRequested);
        } catch (IOException e) {
            view.display(e.getMessage());
        }
    }

    /**
     * Traite la commande entrée par l'utilisateur.
     */
    protected boolean processCommand(String cmd) {
        return switch (cmd) {
            case "h" -> {
                displayHelp();
                yield false;
            }
            case "q" -> true;
            case "r" -> {
                probeState.increaseProbeValue();
                yield false;
            }
            case "m" -> {
                probeState.decreaseProbeValue();
                yield false;
            }
            case "s"->{
                switchProbe();
                yield false;
            }
            default -> {
                displayUnknownCommand();
                yield false;
            }
        };
    }

    protected void switchProbe(){
        probeState.switchProbe();
    }

    /**
     * Affiche l'aide pour les commandes.
     */
    protected void displayHelp() {
        view.display("l - List the commands");
        view.display("m - Mitigate the generated value");
        view.display("r - Raise the generated value");
        view.display("s - change probe");
        view.display("q - quit the application");

    }


    /**
     * Affiche une commande inconnue.
     */
    protected void displayUnknownCommand() {
        view.display("Unknown command. Type 'h' for help.");
    }

    /**
     * Gère et délègue l'affichage des alertes à la vue.
     */
    @Override
    public void alertTriggeredTemperature(String alertType, double expectedTemperature, double difference) {
        view.displayAlertTemperature(alertType, expectedTemperature, difference);
    }

    /**
     * Gère et délègue l'affichage des alertes à la vue.
     */
    @Override
    public void alertTriggeredHumidity(String alertType, double expectedHumidity, double difference) {
        view.displayAlertHumidity(alertType, expectedHumidity, difference);
    }
}
