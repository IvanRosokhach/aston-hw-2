package ru.aston.repository;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.entity.Post;
import ru.aston.entity.User;
import ru.aston.exception.RepositoryException;
import ru.aston.repository.impl.PostRepositoryImpl;
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
import static ru.aston.TestObjectsBuilder.getPost;
import static ru.aston.TestObjectsBuilder.getUser;
import static ru.aston.util.PropertiesLoader.TEST_URL_KEY;
import static ru.aston.util.PropertiesLoader.getProperty;

class PostRepositoryTest {

    private PostRepositoryImpl repository;
    private UserRepositoryImpl userRepository;
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
        repository = new PostRepositoryImpl();
        userRepository = new UserRepositoryImpl();
    }

    @Test
    void create() {
        User user = getUser();
        User expectedUser = userRepository.create(user);
        Post post = getPost();

        Post actualPost = repository.create(post);

        assertEquals(expectedUser.getId(), actualPost.getAuthorId());
        assertEquals(post.getText(), actualPost.getText());
        assertEquals(post.getId(), actualPost.getId());
    }

    @Test
    void findById() {
        User user = getUser();
        User expectedUser = userRepository.create(user);
        Post post = getPost();
        Post expectedPost = repository.create(post);

        Post actualPost = repository.findById(1);

        assertEquals(expectedUser.getId(), actualPost.getAuthorId());
        assertEquals(expectedPost.getText(), actualPost.getText());
        assertEquals(expectedPost.getId(), actualPost.getId());
    }

    @Test
    void findByAuthorId() {
        User user = getUser();
        User expectedUser = userRepository.create(user);
        Post post = getPost();
        Post expectedPost = repository.create(post);

        List<Post> postByAuthorId = repository.findByAuthorId(expectedUser.getId());

        Post actualPost = postByAuthorId.get(0);
        assertEquals(expectedUser.getId(), actualPost.getAuthorId());
        assertEquals(expectedPost.getText(), actualPost.getText());
        assertEquals(expectedPost.getId(), actualPost.getId());
    }

    @Test
    void update() {
        User user = getUser();
        User expectedUser = userRepository.create(user);
        Post post = getPost();
        Post expectedPost = repository.create(post);
        expectedPost.setText("Updated");

        Post actualPost = repository.update(post);

        assertEquals(expectedUser.getId(), actualPost.getAuthorId());
        assertEquals("Updated", actualPost.getText());
        assertEquals(expectedPost.getId(), actualPost.getId());
    }

    @Test
    void deleteById() {
        User user = getUser();
        User actualUser = userRepository.create(user);
        Post post = getPost();
        Post actualPost = repository.create(post);

        assertTrue(repository.deleteById(actualPost.getId()));
        assertThrows(RepositoryException.class, () -> repository.findById(1));
    }

}