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

public class TemperatureMapperTest {

    private Connection connection;
    private TemperatureMapper mapper;

    @BeforeEach
    public void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby:../dbTest;create=true");
        setupDatabase(connection);
        mapper = new TemperatureMapper();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DROP TABLE Temperature");
        ps.executeUpdate();

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void setupDatabase(Connection conn) throws SQLException {
        // Créez la table Temperature
        String createTableSQL = "CREATE TABLE Temperature (id BIGINT GENERATED ALWAYS AS IDENTITY, thermometer_name VARCHAR(255), temperature DOUBLE, timestamp TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(createTableSQL)) {
            ps.executeUpdate();
        }
    }

    @Test
    public void testInsert() throws SQLException {
        // Créez un objet Measurement pour le test
        Measurement testMeasurement = new Measurement("thermometer1", 23.5, LocalDateTime.now());

        // Exécutez la méthode insert
        long generatedId = mapper.insert(connection, testMeasurement);

        // Vérifiez que l'ID généré est valide
        assertTrue(generatedId > 0, "Generated ID should be greater than 0");
    }

    // Ajoutez d'autres tests si nécessaire
}
