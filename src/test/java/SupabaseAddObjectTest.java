/**
 * @author Group 18
 */
import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SupabaseAddObjectTest {
    @Test
    public void addUser() throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        Statement statement = db.connect().createStatement();

        String username = "username";
        String password = "password";
        String fullname = "fullname";
        String dob = "12/12/2012";
        String phone_num = "123456789";
        String email = "email@gmail.com";
        String role = "Tenant";

        // Step 3: Execute a query
        String query = "INSERT INTO public.user (username, password, fullname, dob, phone_num, email, role)" +
                "VALUES ('" + username + "', '" + password + "', '" +
                fullname + "', '" + dob + "', '" +
                phone_num + "', '" + email + "', '" + role + "')";

        assertThrows(PSQLException.class, () -> {
            statement.executeQuery(query);
        }, "\"org.postgresql.util.PSQLException: No results were returned by the query.\" expected.");

    }

    @Test
    public void addResidentProp() throws IOException {

    }
}
