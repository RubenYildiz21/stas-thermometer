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
public class MeasurementAggregator implements Observer {

    private final List<Measurement> measurements = new ArrayList<>();
    private final List<Humidity> humidities = new ArrayList<>();
    private final ObserverManager observerManager;
    private boolean newMeasurmentAdded = false;
    private final Profiles profiles;
    private final String thermometerName;
    private final DatabaseManager<Measurement> measurementDatabaseManager;
    private final DatabaseManager<Humidity> humidityDatabaseManager;


    public MeasurementAggregator(ObserverManager observerManager, Profiles profiles, String thermometerName, DatabaseManager<Measurement> measurementDatabaseManager, DatabaseManager<Humidity> humidityDatabaseManager) {
        this.observerManager = observerManager;
        this.profiles = profiles;
        this.thermometerName = thermometerName;
        this.measurementDatabaseManager = measurementDatabaseManager;
        this.humidityDatabaseManager = humidityDatabaseManager;
    }

    /**
     * Ajoute une nouvelle mesure à la liste des mesures.
     *
     * @param measurement la nouvelle mesure à ajouter.
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
        newMeasurmentAdded = true;
        notifyObservers(measurement.temperature());

    }

    public void addHumidityMeasurement(Humidity humidity){
        humidities.add(humidity);
        newMeasurmentAdded = true;
        notifyHumidityObservers(humidity.humidity());
    }

    /**
     * Calcule la mesure moyenne des températures enregistrées.
     *
     * @return la mesure moyenne ou null si aucune mesure n'est enregistrée.
     */
    protected Measurement getAverageMeasurement() {
        if (measurements.isEmpty()){
            return null;
        }

        double sum = measurements.stream()
                .mapToDouble(Measurement::temperature)
                .sum();

        double average = sum / measurements.size();

        LocalDateTime firstTimestamp = measurements.get(0).timestamp();
        String thermometerName = measurements.get(0).thermometerName();
        measurements.clear();
        return new Measurement(thermometerName,average, firstTimestamp);
    }

    protected Humidity getAverageHumidity(){
        if(humidities.isEmpty()){
            return null;
        }
        double sum = humidities.stream().mapToDouble(Humidity::humidity).sum();
        double average = sum / humidities.size();
        LocalDateTime firstTimeStamp = humidities.get(0).timestamp();
        String thermometerName = humidities.get(0).thermometerName();
        humidities.clear();
        return new Humidity(thermometerName,average, firstTimeStamp);
    }

    /**
     * Calcule la mesure moyenne et notifie les observateurs si une mesure moyenne est disponible.
     */
    public void calculateAndNotifyAverageTemperature() {
        Measurement averageMeasurement = getAverageMeasurement();
        if (averageMeasurement != null) {
            observerManager.notifyAverageTemperatureObservers(averageMeasurement);
            DataMapper<Measurement> operation = new TemperatureMapper();
            long measurementId = measurementDatabaseManager.executeOperation(operation, averageMeasurement);
            checkAndTriggerTemperatureAlert(averageMeasurement, measurementId);
        }
    }

    public void calculateAndNotifyAverageHumidity() {
        Humidity averageHumidity = getAverageHumidity();
        if (averageHumidity != null) {
            observerManager.notifyAverageHumidityObservers(averageHumidity);
            DataMapper<Humidity> operation = new HumidityMapper();
            long humidityId = humidityDatabaseManager.executeOperation(operation, averageHumidity);
            checkAndTriggerHumidityAlert(averageHumidity, humidityId);
        }

    }

    private void checkAndTriggerTemperatureAlert(Measurement averageMeasurement, long measurementId) {
        double expectedTemperature = profiles.getExpectedTemperature(averageMeasurement.timestamp());
        double difference = Math.abs(expectedTemperature - averageMeasurement.temperature());
        if (difference > expectedTemperature * 0.1 && difference > 10) {
            String alertType = averageMeasurement.temperature() > expectedTemperature ? "Surchauffe" : "Refroidissement";
            AlertsTemperatureMapper alertsTemperatureMapper = new AlertsTemperatureMapper(measurementId, difference);
            measurementDatabaseManager.executeOperation(alertsTemperatureMapper, averageMeasurement);
            alertTriggeredTemperature(alertType, expectedTemperature, difference);
        }
    }

    private void checkAndTriggerHumidityAlert(Humidity averageHumidity, long humidityId) {
        double expectedHumidity = profiles.getExpectedHumidity(averageHumidity.timestamp());
        double difference = Math.abs(expectedHumidity - averageHumidity.humidity());
        if (difference > expectedHumidity * 0.1 && difference > 0.1) {
            String alertType = averageHumidity.humidity() > expectedHumidity ? "Trop humide" : "Trop sec";
            AlertsHumidityMapper alertsHumidityMapper = new AlertsHumidityMapper(humidityId, difference);
            humidityDatabaseManager.executeOperation(alertsHumidityMapper, averageHumidity);
            alertTriggeredHumidity(alertType, expectedHumidity, difference);
        }
    }

    public boolean hasNewMeasurement(){
        boolean hasNew = newMeasurmentAdded;
        newMeasurmentAdded = false;
        return hasNew;
    }

    @Override
    public void updateTemperature(double temperature) {
        Measurement measurement = new Measurement(thermometerName, temperature, LocalDateTime.now());
        addMeasurement(measurement);
        observerManager.notifyTemperatureObservers(temperature);
    }

    @Override
    public void updateHumidity(double humidity) {
        Humidity measurement = new Humidity(thermometerName,humidity, LocalDateTime.now());
        addHumidityMeasurement(measurement);
        observerManager.notifyHumidityObservers(humidity);
    }


    @Override
    public void addObserver(Observer observer) {
        observerManager.addTemperatureObserver(observer);
    }

    @Override
    public void addAlertObserver(Observer observer){
        observerManager.addAlertObserver(observer);
    }

    @Override
    public void notifyObservers(double temperature) {
        observerManager.notifyTemperatureObservers(temperature);
    }

    @Override
    public void notifyHumidityObservers(double humidity) {
        observerManager.notifyHumidityObservers(humidity);
    }

    @Override
    public void displayThermometerName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alertTriggeredTemperature(String alertType, double expectedTemperature, double difference) {
        observerManager.notifyTemperatureAlert(alertType, expectedTemperature, difference);
    }

    @Override
    public void alertTriggeredHumidity(String alertType, double expectedHumidity, double difference) {
        observerManager.notifyHumidityAlert(alertType, expectedHumidity, difference);
    }

}