package stas.thermometer.domains;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Profiles {

    private final List<Jalons> jalonsTemperature;
    private final List<Jalons> jalonsHumidity;

    public Profiles(List<Jalons> jalonsTemperature, List<Jalons> jalonsHumidity) {
        this.jalonsTemperature = Collections.unmodifiableList(
                Objects.requireNonNull(jalonsTemperature, "La liste des jalons de température ne peut être nulle"));
        this.jalonsHumidity = Collections.unmodifiableList(
                Objects.requireNonNull(jalonsHumidity, "La liste des jalons d'humidité ne peut être nulle"));
    }


    public double getExpectedTemperature(LocalDateTime time){
        if(jalonsTemperature.isEmpty()) {
            return 0;
        }
        return calculateExpectedValue(time, jalonsTemperature);
    }

    public double getExpectedHumidity(LocalDateTime time){
        if(jalonsTemperature.isEmpty()) {
            return 0;
        }
        return calculateExpectedValue(time, jalonsHumidity);
    }

    private double calculateExpectedValue(LocalDateTime time, List<Jalons> jalons) {
        if(jalonsTemperature.isEmpty()) {
            return 0;
        }
        int indexJalon = getIndexForTime(time, jalons);
        Jalons currentJalon = jalons.get(indexJalon);
        Jalons nextJalon = jalons.get((indexJalon + 1) % jalons.size());
        return interpolateValue(time, currentJalon, nextJalon, jalons);
    }

    private int getIndexForTime(LocalDateTime time, List<Jalons> jalons) {
        if(jalonsTemperature.isEmpty()) {
            return 0;
        }
        double hoursPerJalon = 24.0/jalons.size();
        return time.getHour() / (int) hoursPerJalon;
    }

    private double interpolateValue(LocalDateTime time, Jalons currentJalon, Jalons nextJalon, List<Jalons> jalons) {
        if (jalons.isEmpty()){
            return 0;
        }
        double totalSecondsInJalon = getSecondsSinceJalon(time, currentJalon) + getSecondsUntilJalon(time, nextJalon, jalons);
        if (totalSecondsInJalon == 0 ){
            throw new ArithmeticException("Division par 0 lors du calcul de la position relative");
        }
        double relativePosition = (double) getSecondsSinceJalon(time, currentJalon) / totalSecondsInJalon;
        double slope = (nextJalon.value() - currentJalon.value());

        double interpolateValue = currentJalon.value() + (slope * relativePosition);
        if(interpolateValue < 0){
            throw new ArithmeticException("Valeur interpolée négative");
        }
        return interpolateValue;
    }

    private long getSecondsSinceJalon(LocalDateTime time, Jalons jalon) {
        long secondsSinceJalon = time.toLocalTime().toSecondOfDay() - jalon.time().toLocalTime().toSecondOfDay();
        if (secondsSinceJalon < 0) {
            secondsSinceJalon += 24 * 3600;
        }
        return secondsSinceJalon;
    }

    private long getSecondsUntilJalon(LocalDateTime time, Jalons nextJalon, List<Jalons> jalons) {
        if(jalons.isEmpty()) {
            return 0;
        }
        long secondsUntilJalon = nextJalon.time().toLocalTime().toSecondOfDay() - time.toLocalTime().toSecondOfDay();
        if (secondsUntilJalon < 0) {
            // Gérer le cas où le jalon suivant est le jour suivant
            secondsUntilJalon += 24 * 3600;
        } else if (secondsUntilJalon == 0) {
            // Gérer le cas où le jalon suivant est le premier jalon du jour suivant
            secondsUntilJalon = 24 * 3600 - time.toLocalTime().toSecondOfDay() + jalons.get(0).time().toLocalTime().toSecondOfDay();
        }
        return secondsUntilJalon;
    }

}
