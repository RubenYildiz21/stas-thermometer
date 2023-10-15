package stas.thermometer.app;

import org.junit.jupiter.api.Test;
import stas.thermometer.domains.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    void dateTimeFormat() {
        Configuration config = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        assertEquals("dd-MM-yyyy HH:mm:ss", config.dateTimeFormat());
    }

    @Test
    void temperatureFormat() {
        Configuration config = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        assertEquals("#.##", config.temperatureFormat());
    }

    @Test
    void thermometerName() {
        Configuration config = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        assertEquals("TestThermometer", config.thermometerName());
    }

    @Test
    void testEquals() {
        Configuration config1 = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        Configuration config2 = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        assertEquals(config1, config2);
    }

    @Test
    void testHashCode() {
        Configuration config1 = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        Configuration config2 = new Configuration("dd-MM-yyyy HH:mm:ss", "#.##", "TestThermometer");
        assertEquals(config1.hashCode(), config2.hashCode());
    }
}