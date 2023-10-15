package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggrège et gère les mesures de température et notifie les observateurs.
 *
 * <p>Cette classe est responsable de la collecte des mesures de température, du calcul de la température moyenne
 * et de la notification des observateurs des changements de température et des mesures moyennes.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public class MeasurementAggregator implements TemperatureObserver {

    private final List<Measurement> measurements = new ArrayList<>();
    private final List<TemperatureObserver> observers = new ArrayList<>();
    private final List<AverageMeasurementObserver> averageObservers = new ArrayList<>();

    /**
     * Ajoute une nouvelle mesure à la liste des mesures.
     *
     * @param measurement la nouvelle mesure à ajouter.
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    /**
     * Calcule la mesure moyenne des températures enregistrées.
     *
     * @return la mesure moyenne ou null si aucune mesure n'est enregistrée.
     */
    public Measurement getAverageMeasurement() {
        if (measurements.isEmpty()){
            return null;
        }

        double sum = measurements.stream()
                .mapToDouble(Measurement::temperature)
                .sum();

        double average = sum / measurements.size();

        LocalDateTime firstTimestamp = measurements.get(0).timestamp();
        measurements.clear();
        return new Measurement(average, firstTimestamp);
    }

    /**
     * Ajoute un observateur de mesure moyenne à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    public void addAverageObserver(AverageMeasurementObserver observer) {
        averageObservers.add(observer);
    }

    /**
     * Notifie tous les observateurs de mesure moyenne avec la mesure moyenne calculée.
     *
     * @param averageMeasurement la mesure moyenne calculée.
     */
    public void notifyAverageObservers(Measurement averageMeasurement) {
        for (AverageMeasurementObserver observer : averageObservers) {
            observer.updateAverageMeasurement(averageMeasurement);
        }
    }

    /**
     * Calcule la mesure moyenne et notifie les observateurs si une mesure moyenne est disponible.
     */
    public void calculateAndNotifyAverage() {
        Measurement averageMeasurement = getAverageMeasurement();
        if (averageMeasurement != null) {
            notifyAverageObservers(averageMeasurement);
        }
    }

    @Override
    public void updateTemperature(double temperature) {
        Measurement measurement = new Measurement(temperature, LocalDateTime.now());
        addMeasurement(measurement);
    }

    @Override
    public void addObserver(TemperatureObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(double temperature) {
        for (TemperatureObserver observer : observers) {
            observer.updateTemperature(temperature);
        }
    }

    @Override
    public void displayThermometerName() {
        // Méthode non implémentée
    }

    @Override
    public void alertTriggered(String alertType, double expectedTemperature, double difference) {
        // Méthode non implémentée
    }

}
