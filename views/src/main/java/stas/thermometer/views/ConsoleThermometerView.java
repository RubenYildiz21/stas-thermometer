package stas.thermometer.views;

import stas.thermometer.presentations.ThermometerView;

/**
 * Implémentation de l'interface ThermometerView pour afficher les informations du thermomètre dans la console.
 * Cette classe fournit des méthodes pour afficher les données de température, le nom du thermomètre, des messages généraux et des alertes dans la console.
 */
public class ConsoleThermometerView implements ThermometerView {

    /**
     * Affiche la température actuelle dans la console.
     *
     * @param s La chaîne représentant la température à afficher.
     */
    @Override
    public void displayTemperature(String s) {
        System.out.println(s);
    }

    @Override
    public void displayHumidity(String s) {
        System.out.println(s);
    }

    /**
     * Affiche le nom du thermomètre dans la console.
     *
     * @param s La chaîne représentant le nom du thermomètre à afficher.
     */
    @Override
    public void displayThermometerName(String s){
        System.out.println(s);
    }

    /**
     * Affiche un message général dans la console.
     *
     * @param s La chaîne représentant le message à afficher.
     */
    @Override
    public void display(String s) {
        System.out.println(s);
    }

    /**
     * Affiche une alerte concernant la température dans la console.
     *
     * @param alertType Le type d'alerte à afficher.
     * @param expectedValue La température attendue au moment de l'alerte.
     * @param difference La différence de température qui a déclenché l'alerte.
     */
    @Override
    public void displayAlertTemperature(String alertType, double expectedValue, double difference) {
        System.out.printf("Alert : %s. Température attendue : %.2f°C, écart : %.2f°C\n", alertType, expectedValue, difference);
    }

    /**
     * Affiche une alerte concernant la température dans la console.
     *
     * @param alertType Le type d'alerte à afficher.
     * @param expectedValue La température attendue au moment de l'alerte.
     * @param difference La différence de température qui a déclenché l'alerte.
     */
    @Override
    public void displayAlertHumidity(String alertType, double expectedValue, double difference) {
        System.out.printf("Alert : %s. Humidité attendue : %.2f%%, écart : %.2f%%\n", alertType, expectedValue*100, difference*100);
    }

}
