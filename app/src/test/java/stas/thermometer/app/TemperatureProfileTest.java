package stas.thermometer.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stas.thermometer.domains.TemperatureJalons;
import stas.thermometer.domains.TemperatureProfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TemperatureProfileTest {

    private TemperatureProfile temperatureProfile;
    private List<TemperatureJalons> jalons;

    @BeforeEach
    public void setUp() {
        // Créer des instances réelles de TemperatureJalons avec les valeurs souhaitées
        TemperatureJalons jalon1 = new TemperatureJalons(LocalDateTime.of(2023, 10, 1, 6, 0), 10.0); // 6 AM, 10 degrés
        TemperatureJalons jalon2 = new TemperatureJalons(LocalDateTime.of(2023, 10, 1, 18, 0), 20.0); // 6 PM, 20 degrés

        jalons = Arrays.asList(jalon1, jalon2);
        temperatureProfile = new TemperatureProfile(jalons);
    }
    @Test
    public void testGetExpectedTemperature_Morning() {
        LocalDateTime testTime = LocalDateTime.of(2023, 10, 1, 9, 0); // 9h00
        double expectedTemperature = temperatureProfile.getExpectedTemperature(testTime);

        // Valider que la température attendue est calculée correctement basée sur les jalons
        assertEquals(12.5, expectedTemperature, "La température attendue à 9h devrait être de 12.5 degrés.");
    }

    @Test
    public void testGetExpectedTemperature_Evening() {
        LocalDateTime testTime = LocalDateTime.of(2023, 10, 1, 21, 0); // 9 PM
        double expectedTemperature = temperatureProfile.getExpectedTemperature(testTime);

        // Valider que la température attendue est calculée correctement basée sur les jalons
        assertEquals(17.5, expectedTemperature, "La température attendue à 21h devrait être de 17.5 degrés.");
    }

}

