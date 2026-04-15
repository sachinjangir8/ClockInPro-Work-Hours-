package com.clockinpro.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Modify these credentials according to your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/clockinpro";
    private static final String USER = "root";
    private static final String PASSWORD = "sachin";

    public static Connection getConnection() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database Connection Failed! Make sure MySQL is running and credentials are correct.");
            e.printStackTrace();
            return null;
        }
    }
}
