package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AlertsTemperatureMapperTest {

    private Connection connection;
    private AlertsTemperatureMapper mapper;

    @BeforeEach
    public void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby:../dbTest;create=true");
        setupDatabase(connection);
        mapper = new AlertsTemperatureMapper(123L, 0.5);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DROP TABLE AlertsTemperature");
        ps.executeUpdate();
        connection.close();
    }

    private void setupDatabase(Connection conn) throws SQLException {
        // Créez la table AlertsTemperature
        String createTableSQL = "CREATE TABLE AlertsTemperature (id BIGINT GENERATED ALWAYS AS IDENTITY, type VARCHAR(255), difference DOUBLE, timestamp TIMESTAMP, temperature_id BIGINT)";
        try (PreparedStatement ps = conn.prepareStatement(createTableSQL)) {
            ps.executeUpdate();
        }
    }

    @Test
    public void shouldTestInsertCorrectly() throws SQLException {
        // Créez un objet Measurement pour le test
        Measurement testMeasurement = new Measurement("thermometer1", 25.0, LocalDateTime.now());

        // Exécutez la méthode insert
        long generatedId = mapper.insert(connection, testMeasurement);

        // Vérifiez que l'ID généré est valide
        assertTrue(generatedId > 0, "Generated ID should be greater than 0");
    }
    @Test
    public void shouldTestInsertWithNullValue() {
        assertThrows(NullPointerException.class, () -> {
            mapper.insert(connection, new Measurement(null, 0, null));
        }, "L'insertion d'une valeur nulle devrait lancer une NullPointerException");
    }
}
