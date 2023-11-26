package stas.thermometer.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseManagerTest {

    private DatabaseManager<Object> databaseManager;
    private final String url = "jdbc:derby:../dbTest"; // Vérifiez ce chemin
    private final String user = "root";
    private final String password = "root";

    @Mock
    private DataMapper<Object> mockMapper;

    @BeforeEach
    public void setUp() throws SQLException{
        MockitoAnnotations.openMocks(this);
        databaseManager = new DatabaseManager<>(url, user, password);
    }

    @Test
    public void testConnection() {
        assertTrue(databaseManager.testConnection(), "La connexion à la base de données a échoué");
    }

    @Test
    public void executeOperationShouldInsertData() throws SQLException {
        Object mockItem = new Object();
        when(mockMapper.insert(any(), eq(mockItem))).thenReturn(1L);

        long result = databaseManager.executeOperation(mockMapper, mockItem);
        assertEquals(1L, result, "L'insertion a échoué");
    }

    @Test
    public void shouldHandleTransactionOnFailure() throws SQLException {
        Object mockItem = new Object();
        when(mockMapper.insert(any(), eq(mockItem))).thenReturn(0L);

        assertThrows(RuntimeException.class,
                () -> databaseManager.executeOperation(mockMapper, mockItem),
                "La gestion des transactions en cas d'échec n'est pas correcte");
    }

    @Test
    public void shouldTestConnectionFailure() {
        DatabaseManager<Object> dbManager = new DatabaseManager<>("jdbc:invalidUrl", "wrongUser", "wrongPassword");
        assertFalse(dbManager.testConnection(), "La méthode testConnection devrait retourner false en cas d'échec de la connexion à la base de données");
    }
}
