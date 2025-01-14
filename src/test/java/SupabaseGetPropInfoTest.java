/**
 * @author Group 18
 */
import edu.rmit_hanoi.assignment2.Database.DatabaseConnector;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SupabaseGetPropInfoTest {
    @Test
    public void retrievePropertyData() throws SQLException {
        DatabaseConnector connection = new DatabaseConnector();
        Statement testStatement = connection.connect().createStatement();
        String query = "SELECT * " +
                "FROM public.property p " +
                "JOIN public.commercial_property cp ON p.property_id = cp.property_id " +
                "WHERE status = 'Available'";
        System.out.println("Executed query: " + query);

        ResultSet matchedProperties = testStatement.executeQuery(query);
        assertNotNull(matchedProperties, "Properties retrieved successfully");
    }
}
