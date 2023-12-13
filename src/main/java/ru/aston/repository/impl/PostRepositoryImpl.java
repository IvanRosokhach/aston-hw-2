package ru.aston.repository.impl;

import ru.aston.entity.Post;
import ru.aston.exception.RepositoryException;
import ru.aston.repository.PostRepository;
import ru.aston.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements PostRepository {

    public static final String CREATE_SQL_QUERY = "INSERT INTO posts(text, author_id) VALUES(?, ?);";
    public static final String FIND_BY_ID_SQL_QUERY = "SELECT * FROM posts WHERE post_id = ?;";
    public static final String FIND_BY_AUTHOR_ID_SQL_QUERY = "SELECT * FROM posts WHERE author_id = ?";
    public static final String DELETE_BY_ID_SQL_QUERY = "DELETE FROM posts WHERE post_id = ?;";

    @Override
    public Post create(Post post) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CREATE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, post.getText());
            stmt.setLong(2, post.getAuthorId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows <= 0) {
                throw new RepositoryException("Post doesn't create.");
            }

            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                long id = result.getLong(1);
                post.setId(id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return post;
    }

    @Override
    public Post findById(long postId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_SQL_QUERY)) {
            stmt.setLong(1, postId);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                throw new RepositoryException("POST NOT FIND.");
            }
            return map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Post> findByAuthorId(long authorId) {
        List<Post> list = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_AUTHOR_ID_SQL_QUERY)) {
            stmt.setLong(1, authorId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                list.add(map(resultSet));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean deleteById(long postId) {
        try (Connection connect = ConnectionManager.getConnection();
             PreparedStatement stmt = connect.prepareStatement(DELETE_BY_ID_SQL_QUERY)) {
            stmt.setLong(1, postId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Post map(ResultSet resultSet) throws SQLException {
        return Post.builder()
                .id(resultSet.getLong("post_id"))
                .text(resultSet.getString("text"))
                .authorId(resultSet.getLong("author_id"))
                .build();
    }

}
