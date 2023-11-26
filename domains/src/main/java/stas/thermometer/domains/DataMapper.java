package stas.thermometer.domains;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DataMapper<T> {
    long insert(Connection conn, T item) throws SQLException;
}
