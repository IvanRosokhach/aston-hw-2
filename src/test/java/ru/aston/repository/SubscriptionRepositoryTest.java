package ru.aston.repository;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.entity.User;
import ru.aston.repository.impl.SubscriptionRepositoryImpl;
import ru.aston.repository.impl.UserRepositoryImpl;
import ru.aston.util.ConnectionPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.aston.TestObjectsBuilder.getUser;
import static ru.aston.util.PropertiesLoader.TEST_URL_KEY;
import static ru.aston.util.PropertiesLoader.getProperty;

class SubscriptionRepositoryTest {

    private UserRepositoryImpl userRepository;
    private SubscriptionRepositoryImpl repository;
    private static String schema;

    @BeforeAll
    static void setSource() throws IOException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(getProperty(TEST_URL_KEY));
        ConnectionPool.setDataSource(dataSource);

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/schema.sql"))) {
            schema = br.lines().collect(Collectors.joining());
        }
    }

    @BeforeEach
    void refresh() throws SQLException {
        try (Connection con = ConnectionPool.getConnection()) {
            con.prepareStatement("DROP ALL OBJECTS").execute();
            con.prepareStatement(schema).execute();
        }
        userRepository = new UserRepositoryImpl();
        repository = new SubscriptionRepositoryImpl();
    }

    @Test
    void addAndGet() {
        User user = getUser();
        User user1 = userRepository.create(user);
        user.setName("TestName2");
        user.setLogin("TestLogin2");
        User user2 = userRepository.create(user);

        assertTrue(repository.add(user1.getId(), user2.getId()));
        assertEquals(1, repository.getSubscribers(user1.getId()).size());
    }

    @Test
    void removeAndGet() {
        User user = getUser();
        User user1 = userRepository.create(user);
        user.setName("TestName2");
        user.setLogin("TestLogin2");
        User user2 = userRepository.create(user);

        assertTrue(repository.add(user1.getId(), user2.getId()));
        assertTrue(repository.remove(user1.getId(), user2.getId()));
        assertTrue(repository.getSubscribers(user1.getId()).isEmpty());
    }

}