package stas.thermometer.app;

import stas.thermometer.views.ConsoleThermometerView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleThermometerViewTest {

    private final ConsoleThermometerView view = new ConsoleThermometerView();
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void displayTemperature() {
        view.displayTemperature("25.0°C");
        assertEquals("25.0°C\n", outContent.toString());
    }

    @Test
    void displayThermometerName() {
        view.displayThermometerName("TestThermometer");
        assertEquals("TestThermometer\n", outContent.toString());
    }

    @Test
    void display() {
        view.display("General Message");
        assertEquals("General Message\n", outContent.toString());
    }

    @Test
    void displayAlert() {
        view.displayAlert("surchauffe", 25.0, 15.0);
        assertEquals("Alert : surchauffe. Température attendue : 25,00°C, écart : 15,00°C\n", outContent.toString());
    }
}
