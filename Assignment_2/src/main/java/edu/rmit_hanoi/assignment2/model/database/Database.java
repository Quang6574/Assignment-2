package edu.rmit_hanoi.assignment2.model.database;

import java.sql.*;

public class Database {
    private final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?user=postgres.uqxnmotyhrecepmbkiox&password=c4P2pV*NTu@SZ5M";
    private final String USER = "postgres";
    private final String PASSWORD = "c4P2pV*NTu@SZ5M";
    public Connection connection = null;

    public Database() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void showData() throws SQLException {
        Statement statement = null;
        // Step 2: Create a statement
        statement = connection.createStatement();

        // Step 3: Execute a query
        String query = "SELECT * FROM public.property"; // Replace with your actual table name
        ResultSet resultSet = statement.executeQuery(query);
        // Step 4: Process the result set
        while (resultSet.next()) {
            // Example: Replace with your column names

            System.out.println(resultSet.getString("id"));
        }
    }


}
