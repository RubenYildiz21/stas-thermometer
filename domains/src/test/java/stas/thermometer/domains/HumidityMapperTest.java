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

public class HumidityMapperTest {

    private Connection connection;
    private HumidityMapper mapper;

    @BeforeEach
    public void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby:../dbTest;create=true");
        setupDatabase(connection);
        mapper = new HumidityMapper();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DROP TABLE Humidity");
        ps.executeUpdate();
        connection.close();
    }

    private void setupDatabase(Connection conn) throws SQLException {
        // Créez la table Humidity
        String createTableSQL = "CREATE TABLE Humidity (id BIGINT GENERATED ALWAYS AS IDENTITY, thermometer_name VARCHAR(255), humidity DOUBLE, timestamp TIMESTAMP)";
        try (PreparedStatement ps = conn.prepareStatement(createTableSQL)) {
            ps.executeUpdate();
        }
    }

    @Test
    public void shouldTestInsertCorrectly() throws SQLException {
        // Créez un objet Humidity pour le test
        Humidity testHumidity = new Humidity("thermometer1", 0.45, LocalDateTime.now());

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
