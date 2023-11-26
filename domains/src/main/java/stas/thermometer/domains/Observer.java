package stas.thermometer.domains;

/**
 * Interface définissant les opérations nécessaires pour les classes qui souhaitent observer les changements de température.
 *
 * <p>Cette interface est utilisée pour implémenter le patron de conception observateur, où les objets de type {@code Observer}
 * peuvent s'inscrire pour recevoir des mises à jour liées aux changements de température. Les méthodes fournies permettent
 * l'ajout, la suppression et la notification des observateurs, ainsi que le déclenchement d'alertes basées sur des conditions de température spécifiques.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public interface Observer {

    /**
     * Met à jour la température observée.
     *
     * @param temperature la nouvelle valeur de température.
     */
    void updateTemperature(double temperature);

    /**
     * Met à jour l'humidité' observée.
     *
     * @param humidity la nouvelle valeur de température.
     */
    void updateHumidity(double humidity);

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    void addObserver(Observer observer);


    void addAlertObserver(Observer observer);

    /**
     * Notifie tous les observateurs inscrits d'un changement de température.
     *
     * @param temperature la nouvelle valeur de température qui a déclenché la notification.
     */
    void notifyObservers(double temperature);


    void notifyHumidityObservers(double humidity);

    /**
     * Affiche le nom du thermomètre.
     */
    void displayThermometerName();

    /**
     * Est appelé lorsqu'une alerte est déclenchée en raison d'une différence de température significative.
     *
     * @param alertType le type d'alerte déclenchée (par exemple, "Surchauffe" ou "Refroidissement").
     * @param expectedTemperature la température attendue ou cible.
     * @param difference la différence entre la température attendue et la température actuelle.
     */
    void alertTriggeredTemperature(String alertType, double expectedTemperature, double difference);

    void alertTriggeredHumidity(String alertType, double expectedTemperature, double difference);
}
