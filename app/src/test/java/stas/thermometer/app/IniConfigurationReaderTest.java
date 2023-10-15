package stas.thermometer.app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stas.thermometer.domains.TemperatureJalons;
import stas.thermometer.domains.TemperatureProfile;
import stas.thermometer.infrastructures.IniConfigurationReader;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class IniConfigurationReaderTest {

    private IniConfigurationReader iniConfigurationReader;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        String iniData = "[general]\n"
                       + "name = TestThermometer\n"
                       + "[format]\n"
                       + "datetime = dd/MM/yyyy à HH:mm:ss\n"
                       + "temperature = 20.00°\n"
                       + "[profile]\n"
                       + "jal00=22\n"
                       + "jal01=25\n";
        InputStream inputStream = new ByteArrayInputStream(iniData.getBytes());
        iniConfigurationReader = new IniConfigurationReader(inputStream);
    }

    @Test
    public void testGetThermometerName() {
        String thermometerName = iniConfigurationReader.getThermometerName();
        assertEquals("TestThermometer", thermometerName);
    }

    @Test
    public void testGetDateTimeFormat() {
        String dateTimeFormat = iniConfigurationReader.getDateTimeFormat();
        assertEquals("dd/MM/yyyy à HH:mm:ss", dateTimeFormat);
    }

    @Test
    public void testGetTemperatureFormat() {
        String temperatureFormat = iniConfigurationReader.getTemperatureFormat();
        assertEquals("20.00°", temperatureFormat);
    }

    @Test
    public void testGetTemperatureProfile() {
        TemperatureProfile profile = iniConfigurationReader.getTemperatureProfile();
        assertNotNull(profile, "Le profil de température ne doit pas être null");

        // Supposons que le jalon à 21 heures est de 22.5 degrés
        assertEquals(22, profile.getExpectedTemperature(LocalDateTime.of(2023, 10, 4, 0, 0)),
                "La température attendue à 00h heures devrait être 22.5°");

        // Supposons que le jalon à 12 heures est de 25.0 degrés
        assertEquals(25, profile.getExpectedTemperature(LocalDateTime.of(2023, 10, 4, 12, 0)),
                "La température attendue à 12 heures devrait être 25.0");
    }


    @Test
    public void testGetValue() {
        String value = iniConfigurationReader.getValue("general", "name");
        assertEquals("TestThermometer", value);
    }

    @Test
    public void testGetAllSections() {
        Map<String, Map<String, String>> allSections = iniConfigurationReader.getAllSections();
        assertTrue(allSections.containsKey("general"));
        assertTrue(allSections.containsKey("format"));
        assertTrue(allSections.containsKey("profile"));
    }

}
