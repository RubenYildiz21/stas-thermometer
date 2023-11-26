package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProfilesTest {

    private Profiles profiles;

    @BeforeEach
    void setUp() {
        // Initialiser les listes de Jalons avec des données fictives
        // Autres jalons...
        List<Jalons> jalonsTemperature = Arrays.asList(
                new Jalons(LocalDateTime.of(2023, 11, 21, 6, 0), 20.0),
                new Jalons(LocalDateTime.of(2023, 11, 21, 12, 0), 25.0)
                // Autres jalons...
        );
        // Autres jalons...
        List<Jalons> jalonsHumidity = Arrays.asList(
                new Jalons(LocalDateTime.of(2023, 11, 21, 6, 0), 40.0),
                new Jalons(LocalDateTime.of(2023, 11, 21, 12, 0), 60.0)
                // Autres jalons...
        );

        profiles = new Profiles(jalonsTemperature, jalonsHumidity);
    }

    @Test
    void shouldReturnCorrectExpectedTemperature() {
        LocalDateTime time = LocalDateTime.of(2023, 11, 21, 9, 0); // Un moment entre deux jalons
        double expectedTemperature = profiles.getExpectedTemperature(time);

        // Vérifier si la température calculée est correcte
        assertEquals(22.5, expectedTemperature); // Cette valeur dépend de vos données fictives
    }

    @Test
    void shouldReturnCorrectExpectedHumidity() {
        LocalDateTime time = LocalDateTime.of(2023, 11, 21, 9, 0); // Un moment entre deux jalons
        double expectedHumidity = profiles.getExpectedHumidity(time);

        // Vérifier si l'humidité calculée est correcte
        assertEquals(50.0, expectedHumidity); // Cette valeur dépend de vos données fictives
    }

}
