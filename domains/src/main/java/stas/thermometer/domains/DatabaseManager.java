package stas.thermometer.domains;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager<T> {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            throw new RuntimeException("stas.thermometer : unable to connect to the database", e);
        }
    }


    public long executeOperation(DataMapper<T> mapper, T item) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            long result = mapper.insert(conn, item);
            if(result > 0){
                conn.commit();
                return result;
            }else {
                conn.rollback();
                throw new RuntimeException("Insertion failed, transaction rolled back");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Stas.thermometer : unable to insert in database",e);
        }
    }


    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
