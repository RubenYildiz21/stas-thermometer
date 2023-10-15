package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe représentant un profil de température basé sur des jalons.
 *
 * <p>Cette classe gère une liste de {@code TemperatureJalons} pour déterminer la température attendue
 * à un moment donné en se basant sur les jalons définis.</p>
 *
 * @author Ruben Yildiz
 * @version 1.0
 */
public class TemperatureProfile {

    private final List<TemperatureJalons> jalons;

    /**
     * Constructeur de TemperatureProfile.
     *
     * @param jalons la liste des jalons de température.
     */
    public TemperatureProfile(List<TemperatureJalons> jalons) {
        this.jalons = jalons;
    }

    /**
     * Calcule la température attendue à un moment donné.
     *
     * @param time le moment pour lequel déterminer la température attendue.
     * @return la température attendue.
     */
    public double getExpectedTemperature(LocalDateTime time) {
        int numberOfJalons = jalons.size();
        double hoursPerJalon = 24.0 / numberOfJalons;
        double totalSecondsInJalon = hoursPerJalon * 3600; // Convertir les heures en secondes

        TemperatureJalons[] currentAndNextJalons = getCurrentAndNextJalons(time, numberOfJalons, hoursPerJalon);
        TemperatureJalons currentJalon = currentAndNextJalons[0];
        TemperatureJalons nextJalon = currentAndNextJalons[1];

        double relativePosition = getRelativePosition(time, currentJalon, totalSecondsInJalon);

        double slope = calculateSlope(currentJalon, nextJalon);

        return currentJalon.temperature() + (slope * relativePosition);
    }

    /**
     * Calcule la pente entre deux jalons de température.
     *
     * @param currentJalon le jalon de température actuel.
     * @param nextJalon le prochain jalon de température.
     * @return la pente calculée.
     */
    private double calculateSlope(TemperatureJalons currentJalon, TemperatureJalons nextJalon) {
        return (nextJalon.temperature() - currentJalon.temperature());
    }

    /**
     * Obtient la position relative du temps actuel par rapport au jalon actuel.
     *
     * @param time le moment actuel.
     * @param currentJalon le jalon de température actuel.
     * @param totalSecondsInJalon le total de secondes dans un jalon.
     * @return la position relative calculée.
     */
    private double getRelativePosition(LocalDateTime time, TemperatureJalons currentJalon, double totalSecondsInJalon) {
        long secondsSinceCurrentJalon = time.toLocalTime().toSecondOfDay() - currentJalon.time().toLocalTime().toSecondOfDay();

        // Ajustement si 'time' est avant 'currentJalon' dans la journée
        if (secondsSinceCurrentJalon < 0) {
            secondsSinceCurrentJalon += 24 * 3600; // ajout d'un jour complet en secondes
        }

        return (double) secondsSinceCurrentJalon / totalSecondsInJalon;
    }

    /**
     * Récupère les jalons actuel et suivant basés sur l'heure donnée.
     *
     * @param time l'heure actuelle.
     * @param numberOfJalons le nombre total de jalons.
     * @param hoursPerJalon le nombre d'heures par jalon.
     * @return un tableau contenant les jalons actuel et suivant.
     */
    private TemperatureJalons[] getCurrentAndNextJalons(LocalDateTime time, int numberOfJalons, double hoursPerJalon) {
        int currentHour = time.getHour();
        int indexJalon = currentHour / (int) hoursPerJalon; // changement ici

        // Gérer le cas où nous sommes à la dernière tranche horaire de la journée
        if (indexJalon >= numberOfJalons) {
            indexJalon = numberOfJalons - 1;
        }

        TemperatureJalons currentJalon = jalons.get(indexJalon);
        TemperatureJalons nextJalon = jalons.get((indexJalon + 1) % numberOfJalons);

        return new TemperatureJalons[]{currentJalon, nextJalon};
    }

}
