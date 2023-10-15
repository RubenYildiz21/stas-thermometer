package stas.thermometer.domains;

import java.time.LocalDateTime;

/**
 * Représente un jalon de température avec un moment spécifique et la température associée.
 *
 * <p>Ce record est une construction immuable qui détient un point dans le temps et la
 * température correspondante. Il est typiquement utilisé pour définir des points de référence
 * ou des objectifs dans la gestion de température.</p>
 *
 * @param time le moment spécifique auquel le jalon de température s'applique.
 * @param temperature la valeur de la température au moment spécifié.
 * @author Ruben Yildiz
 * @version 1.0
 */
public record TemperatureJalons(LocalDateTime time, double temperature) {

}
