/**
 * @author Group 18
 */
import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SupabaseConnectionTest {
    @Test
    public void JavaToSupabase() throws SQLException {
        DatabaseConnector connection = new DatabaseConnector();
        assertNotNull(connection.connect(), "Connection established. Username and password are correct. The Supabase free version has not expired");
    }

}
