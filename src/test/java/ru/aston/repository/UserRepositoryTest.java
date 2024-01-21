package ru.aston.repository;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.entity.User;
import ru.aston.exception.RepositoryException;
import ru.aston.repository.impl.UserRepositoryImpl;
import ru.aston.util.ConnectionPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.aston.TestObjectsBuilder.getUser;
import static ru.aston.util.PropertiesLoader.TEST_URL_KEY;
import static ru.aston.util.PropertiesLoader.getProperty;

class UserRepositoryTest {

    private UserRepositoryImpl repository;
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
        repository = new UserRepositoryImpl();
    }

    @Test
    void create() {
        User user = getUser();

        User actualUser = repository.create(user);

        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getLogin(), actualUser.getLogin());
    }

    @Test
    void findById() {
        User user = getUser();
        repository.create(user);

        User actualUser = repository.findById(1);

        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getLogin(), actualUser.getLogin());
    }

    @Test
    void findAll() {
        User user = getUser();
        repository.create(user);
        repository.create(user);

        List<User> actualUserList = repository.findAll();

        assertEquals(2, actualUserList.size());
    }

    @Test
    void update() {
        User user = getUser();
        repository.create(user);
        user.setLogin("Updated");
        user.setName("Updated");

        User actualUser = repository.update(user);

        assertEquals(1, actualUser.getId());
        assertEquals("Updated", actualUser.getName());
        assertEquals("Updated", actualUser.getLogin());
    }

    @Test
    void deleteById() {
        User user = getUser();
        repository.create(user);

        assertTrue(repository.deleteById(1));
        assertTrue(repository.findAll().isEmpty());
        assertThrows(RepositoryException.class, () -> repository.findById(5));
    }

}