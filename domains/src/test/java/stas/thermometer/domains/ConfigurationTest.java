package stas.thermometer.domains;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    private final String dateFormat = "dd-MM-yyyy HH:mm:ss";
    private final String tempFormat = "#.##";
    private final String thermoName = "TestThermometer";
    @Test
    void dateTimeFormat() {
        Configuration config = new Configuration(dateFormat, tempFormat, thermoName);
        assertEquals("dd-MM-yyyy HH:mm:ss", config.dateTimeFormat());
    }

    @Test
    void temperatureFormat() {
        Configuration config = new Configuration(dateFormat, tempFormat, thermoName);
        assertEquals("#.##", config.temperatureFormat());
    }

    @Test
    void thermometerName() {
        Configuration config = new Configuration(dateFormat, tempFormat, thermoName);
        assertEquals("TestThermometer", config.thermometerName());
    }

    @Test
    void testEquals() {
        Configuration config1 = new Configuration(dateFormat, tempFormat, thermoName);
        Configuration config2 = new Configuration(dateFormat, tempFormat, thermoName);
        assertEquals(config1, config2);
    }

    @Test
    void testHashCode() {
        Configuration config1 = new Configuration(dateFormat, tempFormat, thermoName);
        Configuration config2 = new Configuration(dateFormat, tempFormat, thermoName);
        assertEquals(config1.hashCode(), config2.hashCode());
    }
}
