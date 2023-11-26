package stas.thermometer.domains;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager {
    private final List<Observer> temperatureObservers = new ArrayList<>();
    private final List<AverageMeasurementObserver> averageObservers = new ArrayList<>();
    private final List<Observer> alertObservers = new ArrayList<>();

    public void addTemperatureObserver(Observer observer){
        temperatureObservers.add(observer);
    }

    public void addAlertObserver(Observer observer){
        alertObservers.add(observer);
    }

    public void notifyTemperatureObservers(double temperature){
        for(Observer observer : temperatureObservers){
            observer.updateTemperature(temperature);
        }
    }

    public void notifyHumidityObservers(double humidity){
        for(Observer observer : temperatureObservers){
            observer.updateHumidity(humidity);
        }
    }

    public void addAverageObserver(AverageMeasurementObserver observer){
        averageObservers.add(observer);
    }

    public void notifyAverageTemperatureObservers(Measurement averageMeasurement){
        for (AverageMeasurementObserver observer : averageObservers){
            observer.updateAverageMeasurement(averageMeasurement);
        }
    }

    public void notifyAverageHumidityObservers(Humidity averageHumidity){
        for (AverageMeasurementObserver observer : averageObservers){
            observer.updateAverageHumidity(averageHumidity);
        }
    }

    public void notifyTemperatureAlert(String alertType, double expectedTemperature, double difference) {
        for (Observer observer : alertObservers) {
            observer.alertTriggeredTemperature(alertType, expectedTemperature, difference);
        }
    }

    public void notifyHumidityAlert(String alertType, double expectedHumidity, double difference) {
        for (Observer observer : alertObservers) {
            observer.alertTriggeredHumidity(alertType, expectedHumidity, difference);
        }
    }
}
