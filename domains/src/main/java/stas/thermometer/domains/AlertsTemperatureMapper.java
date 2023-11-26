package stas.thermometer.domains;

import java.sql.*;

public class AlertsTemperatureMapper implements DataMapper<Measurement>{

    private final long measurementId;
    private final double difference;
    public AlertsTemperatureMapper(long measurementId, double difference) {
        this.measurementId = measurementId;
        this.difference = difference;
    }

    @Override
    public long insert(Connection conn, Measurement item) throws SQLException {
        String sql = "INSERT INTO AlertsTemperature (type, difference, timestamp, temperature_id) VALUES (?, ?, ?, ?)";
        String type = "Temperature";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Définissez les paramètres ici, y compris measurement_id
            statement.setString(1, type);
            statement.setDouble(2, difference);
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(item.timestamp()));
            statement.setDouble(4, measurementId);
            statement.executeUpdate();
            // Pas besoin de retourner un ID ici, sauf si nécessaire
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Récupère l'ID généré
                } else {
                    throw new SQLException("Creating alert failed, no ID obtained.");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
