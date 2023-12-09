package ru.aston.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.aston.util.Constants.DRIVER_KEY;
import static ru.aston.util.Constants.PASSWORD_KEY;
import static ru.aston.util.Constants.URL_KEY;
import static ru.aston.util.Constants.USERNAME_KEY;

public final class ConnectionManager {

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
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
