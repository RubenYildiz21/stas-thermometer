package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HumidityProbe {

    private final Profiles profile;
    private final List<Observer> observers = new ArrayList<>();
    private double humidityOffset = 0.0;

    /**
     * Constructeur de la sonde de température.
     *
     * @param profile         le profil de température utilisé pour déterminer la température attendue.
     */
    public HumidityProbe(Profiles profile) {
        this.profile = profile;
    }

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void measureAndNotify() {
        double humidity = getAdjustedHumidity();
        notifyObservers(humidity);
    }

    public void notifyObservers(double humidity) {
        for (Observer observer : observers) {
            observer.updateHumidity(humidity);
        }
    }

    /**
     * Récupère l'humidité ajustée en y ajoutant une erreur aléatoire et un décalage.
     *
     * @return la température ajustée.
     */
    public double getAdjustedHumidity(){
        double expectedHumidity = profile.getExpectedHumidity(LocalDateTime.now());
        double randomError = (-5 + new Random().nextDouble() * 10)/100;
        return expectedHumidity + randomError + humidityOffset;
    }

    /**
     * Augmente l'humidité de 5%
     */
    public void increaseHumidity() {
        humidityOffset += 0.04; // Augmente de 5%
    }

    /**
     * Diminue l'humidité de 5%'
     */
    public void decreaseHumidity() {
        humidityOffset -= 0.04; // Diminue de 5%
    }

}
