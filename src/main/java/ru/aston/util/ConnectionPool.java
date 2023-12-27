package ru.aston.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.aston.util.PropertiesLoader.DRIVER_KEY;
import static ru.aston.util.PropertiesLoader.PASSWORD_KEY;
import static ru.aston.util.PropertiesLoader.URL_KEY;
import static ru.aston.util.PropertiesLoader.USERNAME_KEY;
import static ru.aston.util.PropertiesLoader.getProperty;

@UtilityClass
public class ConnectionPool {

    private static final String JDBC_URL = getProperty(URL_KEY);
    private static final String DRIVER_NAME = getProperty(DRIVER_KEY);
    private static final String USERNAME = getProperty(USERNAME_KEY);
    private static final String PASSWORD = getProperty(PASSWORD_KEY);
    private static HikariDataSource dataSource;

    private static void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setDriverClassName(DRIVER_NAME);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            init();
            initializeDatabase();
        }
        return dataSource.getConnection();
    }

    public static void setDataSource(DataSource ds) {
        HikariConfig config = new HikariConfig();
        config.setDataSource(ds);
        dataSource = new HikariDataSource(config);
    }

    private static void initializeDatabase() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(ConnectionPool.class.getClassLoader().getResourceAsStream("schema.sql"))))) {
            String sql = br.lines().collect(Collectors.joining());
            dataSource.getConnection().prepareStatement(sql).execute();

        } catch (IOException e) {
            throw new RuntimeException("Error while loading database schema from file: " + e.getMessage());

        } catch (SQLException e) {
            throw new RuntimeException("Error while executing initial database schema: " + e.getMessage());
        }
    }

}
