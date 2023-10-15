package stas.thermometer.presentations;

/**
 * Interface définissant les méthodes requises pour les classes qui implémentent la vue d'un thermomètre.
 * Elle fournit des méthodes pour afficher les données de température, le nom du thermomètre, des messages généraux et des alertes.
 */
public interface ThermometerView {

    /**
     * Affiche la température actuelle.
     *
     * @param s La chaîne représentant la température à afficher.
     */
    void displayTemperature(String s);

    /**
     * Affiche le nom du thermomètre.
     *
     * @param s La chaîne représentant le nom du thermomètre à afficher.
     */
    void displayThermometerName(String s);

    /**
     * Affiche un message général.
     *
     * @param s La chaîne représentant le message à afficher.
     */
    void display(String s);

    /**
     * Affiche une alerte concernant la température.
     *
     * @param alertType Le type d'alerte à afficher.
     * @param expectedTemperature La température attendue au moment de l'alerte.
     * @param difference La différence de température qui a déclenché l'alerte.
     */
    void displayAlert(String alertType, double expectedTemperature, double difference);
}
