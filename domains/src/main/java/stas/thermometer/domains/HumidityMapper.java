package stas.thermometer.domains;

import java.sql.*;

public class HumidityMapper implements DataMapper<Humidity>{
    @Override
    public long insert(Connection conn, Humidity item) throws SQLException {
        String sql = "INSERT INTO Humidity (thermometer_name, humidity, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.thermometerName());
            statement.setDouble(2, item.humidity()*100);
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(item.timestamp()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Récupère l'ID généré
                } else {
                    throw new SQLException("Creating temperature record failed, no ID obtained.");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
