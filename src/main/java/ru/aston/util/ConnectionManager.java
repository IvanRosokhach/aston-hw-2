package ru.aston.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driverClassName";

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            System.out.println(PropertiesLoader.getProperty(URL_KEY) +
                    PropertiesLoader.getProperty(USERNAME_KEY) +
                    PropertiesLoader.getProperty(PASSWORD_KEY));
            return DriverManager.getConnection(
                    PropertiesLoader.getProperty(URL_KEY),
                    PropertiesLoader.getProperty(USERNAME_KEY),
                    PropertiesLoader.getProperty(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesLoader.getProperty(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
