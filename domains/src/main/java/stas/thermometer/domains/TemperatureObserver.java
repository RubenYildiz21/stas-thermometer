package stas.thermometer.domains;

/**
 * Interface définissant les opérations nécessaires pour les classes qui souhaitent observer les changements de température.
 *
 * <p>Cette interface est utilisée pour implémenter le patron de conception observateur, où les objets de type {@code TemperatureObserver}
 * peuvent s'inscrire pour recevoir des mises à jour liées aux changements de température. Les méthodes fournies permettent
 * l'ajout, la suppression et la notification des observateurs, ainsi que le déclenchement d'alertes basées sur des conditions de température spécifiques.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public interface TemperatureObserver {

    /**
     * Met à jour la température observée.
     *
     * @param temperature la nouvelle valeur de température.
     */
    void updateTemperature(double temperature);

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observer l'observateur à ajouter.
     */
    void addObserver(TemperatureObserver observer);


    /**
     * Notifie tous les observateurs inscrits d'un changement de température.
     *
     * @param temperature la nouvelle valeur de température qui a déclenché la notification.
     */
    void notifyObservers(double temperature);

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
    void alertTriggered(String alertType, double expectedTemperature, double difference);
}
