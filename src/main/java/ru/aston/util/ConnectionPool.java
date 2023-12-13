package ru.aston.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static ru.aston.util.Constants.DRIVER_KEY;
import static ru.aston.util.Constants.PASSWORD_KEY;
import static ru.aston.util.Constants.URL_KEY;
import static ru.aston.util.Constants.USERNAME_KEY;
import static ru.aston.util.PropertiesLoader.getProperty;

public class ConnectionPool {  //TODO

    private static final String JDBC_URL = getProperty(URL_KEY);
    private static final String DRIVER_NAME = getProperty(DRIVER_KEY);
    private static final String USERNAME = getProperty(USERNAME_KEY);
    private static final String PASSWORD = getProperty(PASSWORD_KEY);
    private static HikariDataSource dataSource;

    public static void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setDriverClassName(DRIVER_NAME);
        config.setAutoCommit(false);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            init();
        }
        return dataSource.getConnection();
    }

}
