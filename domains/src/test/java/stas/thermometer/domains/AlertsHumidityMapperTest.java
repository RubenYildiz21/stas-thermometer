package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AlertsHumidityMapperTest {

    private Connection connection;
    private AlertsHumidityMapper mapper;

    @BeforeEach
    public void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby:../dbTest;create=true");
        setupDatabase(connection);
        mapper = new AlertsHumidityMapper(123L, 0.5);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DROP TABLE AlertsHumidity");
        ps.executeUpdate();
        connection.close();
    }

    private void setupDatabase(Connection conn) throws SQLException {

            String createTableSQL = "CREATE TABLE AlertsHumidity (id BIGINT GENERATED ALWAYS AS IDENTITY, type VARCHAR(255), difference DOUBLE, timestamp TIMESTAMP, humidity_id BIGINT)";
            try (PreparedStatement ps = conn.prepareStatement(createTableSQL)) {
                ps.executeUpdate();
            }

    }

    @Test
    public void shouldTestInsertCorrectly() throws SQLException {
        // Créez un objet Humidity pour le test
        Humidity testHumidity = new Humidity("thermometer1", 0.7, LocalDateTime.now());

        // Exécutez la méthode insert
        long generatedId = mapper.insert(connection, testHumidity);

        // Vérifiez que l'ID généré est valide
        assertTrue(generatedId > 0, "Generated ID should be greater than 0");
    }

    @Test
    public void shouldTestInsertWithNullValue() {
        assertThrows(NullPointerException.class, () -> {
            mapper.insert(connection, new Humidity(null, 0, null));
        }, "L'insertion d'une valeur nulle devrait lancer une NullPointerException");
    }

}
