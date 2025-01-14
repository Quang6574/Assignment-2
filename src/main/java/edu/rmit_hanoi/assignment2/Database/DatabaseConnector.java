package edu.rmit_hanoi.assignment2.Database;
/**
 * @author Group 18
 */

import java.sql.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?user=postgres.uqxnmotyhrecepmbkiox&password=c4P2pV*NTu@SZ5M";
    public static Connection connection = null;

    public Connection connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        return connection;
    }
}