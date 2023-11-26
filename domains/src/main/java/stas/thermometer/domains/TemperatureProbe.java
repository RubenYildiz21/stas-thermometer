package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe représentant la sonde de température.
 *
 * <p>Cette classe gère les mesures de température, informe les observateurs des changements,
 * et déclenche des alertes en cas de variations significatives de la température. Elle utilise
 * un {@code Profiles} pour obtenir la température attendue à un moment donné.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public class TemperatureProbe {

    private final Profiles profile;
    private final List<Observer> observers = new ArrayList<>();
    private double temperatureOffset = 0.0;

    /**
     * Constructeur de la sonde de température.
     *
     * @param profile         le profil de température utilisé pour déterminer la température attendue.
     */
    public TemperatureProbe(Profiles profile) {
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

    /**
     * Notifie tous les observateurs d'un changement de température.
     *
     * @param temperature la nouvelle valeur de température.
     */
    public void notifyObservers(double temperature) {
        for (Observer observer : observers) {
            observer.updateTemperature(temperature);
        }
    }

    /**
     * Effectue une mesure de la température et notifie les observateurs.
     */
    public void measureAndNotify() {
        double temperature = getAdjustedTemperature();
        notifyObservers(temperature);
    }

    /**
     * Récupère la température ajustée en y ajoutant une erreur aléatoire et un décalage.
     *
     * @return la température ajustée.
     */
    public double getAdjustedTemperature() {
        double expectedTemperature = profile.getExpectedTemperature(LocalDateTime.now());
        double randomError = (new Random().nextDouble() - 0.5);  // Erreur aléatoire entre -0.5 et 0.5
        return expectedTemperature + randomError + temperatureOffset;
    }



    /**
     * Augmente la température de 0.5 degré.
     */
    public void increaseTemperature(){
        temperatureOffset += 0.5;
    }

    /**
     * Diminue la température de 0.5 degré.
     */
    public void decreaseTemperature(){
        temperatureOffset -= 0.5;
    }

}

