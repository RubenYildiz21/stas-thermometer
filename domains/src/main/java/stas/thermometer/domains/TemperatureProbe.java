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
 * un {@code TemperatureProfile} pour obtenir la température attendue à un moment donné.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public class TemperatureProbe {

    private final TemperatureProfile profile;
    private final List<TemperatureObserver> observers = new ArrayList<>();
    private double temperatureOffset = 0.0;
    private boolean alertTriggered = false;
    private String lastAlertType = "";

    /**
     * Constructeur de la sonde de température.
     *
     * @param profile le profil de température utilisé pour déterminer la température attendue.
     */
    public TemperatureProbe(TemperatureProfile profile) {
        this.profile = profile;
    }

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    public void addObserver(TemperatureObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifie tous les observateurs d'un changement de température.
     *
     * @param temperature la nouvelle valeur de température.
     */
    public void notifyObservers(double temperature) {
        for (TemperatureObserver observer : observers) {
            observer.updateTemperature(temperature);
        }
    }

    /**
     * Effectue une mesure de la température et notifie les observateurs.
     */
    public void measureAndNotify() {
        double temperature = getAdjustedTemperature();
        notifyObservers(temperature);
        checkAndTriggerAlert(temperature);
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

    /**
     * Vérifie et déclenche une alerte si la différence entre la température actuelle et attendue dépasse un certain seuil.
     *
     * @param currentTemperature la température actuelle mesurée par la sonde.
     */
    public void checkAndTriggerAlert(double currentTemperature){
        double expectedTemperature = profile.getExpectedTemperature(LocalDateTime.now());
        double difference = Math.abs(expectedTemperature - currentTemperature);

        if (shouldTriggerAlert(difference, expectedTemperature, currentTemperature)){
            triggerAlert(currentTemperature > expectedTemperature ? "Surchauffe" : "Refroidissement", expectedTemperature, difference);
        } else if (alertTriggered) {
            resetAlertState();
        }
    }

    /**
     * Détermine si une alerte doit être déclenchée en fonction de la différence de température et de l'état actuel de l'alerte.
     *
     * @param difference la différence entre la température actuelle et la température attendue.
     * @param expectedTemperature la température attendue.
     * @param currentTemperature la température actuelle.
     * @return {@code true} si une alerte doit être déclenchée, {@code false} sinon.
     */
    private boolean shouldTriggerAlert(double difference, double expectedTemperature, double currentTemperature) {
        return difference > expectedTemperature * 0.1 && (!alertTriggered || !lastAlertType.equals(currentTemperature > expectedTemperature ? "Surchauffe" : "Refroidissement"));
    }

    /**
     * Déclenche une alerte et notifie tous les observateurs.
     *
     * @param alertType le type d'alerte ("Surchauffe" ou "Refroidissement").
     * @param expectedTemperature la température attendue.
     * @param difference la différence entre la température actuelle et la température attendue.
     */
    private void triggerAlert(String alertType, double expectedTemperature, double difference) {
        alertTriggered = true;
        lastAlertType = alertType;
        for(TemperatureObserver observer : observers){
            observer.alertTriggered(alertType, expectedTemperature, difference);
        }
    }

    /**
     * Réinitialise l'état de l'alerte après qu'elle ait été gérée.
     */
    private void resetAlertState() {
        alertTriggered = false;
        lastAlertType = "";
    }

}
