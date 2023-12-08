package ru.aston.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DbInitializeUtil {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driverClassName";


    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    public static void initDB() {
        Connection conn = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(PropertiesLoader.getProperty(URL_KEY),
                    PropertiesLoader.getProperty(USERNAME_KEY),
                    PropertiesLoader.getProperty(PASSWORD_KEY));
            InputStream scriptIS = DbInitializeUtil.class
                    .getResourceAsStream("/schema.sql");
            executeSqlScript(conn, scriptIS);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void executeSqlScript(Connection conn, InputStream inputFile) {

        String delimiter = ";";

        Scanner scanner;
        try {
            scanner = new Scanner(inputFile).useDelimiter(delimiter);
        } catch (Exception e1) {
            throw new RuntimeException(e1.getMessage());
        }

        Statement currentStatement = null;
        while (scanner.hasNext()) {

            String rawStatement = scanner.next() + delimiter;
            try {
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                if (currentStatement != null) {
                    try {
                        currentStatement.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
                currentStatement = null;
            }
        }
        scanner.close();
    }
}