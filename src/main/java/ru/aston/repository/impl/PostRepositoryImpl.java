package ru.aston.repository.impl;

import ru.aston.entity.Post;
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

    @Override
    public Post create(Post post) {
        String sqlQuery = "INSERT INTO posts(text, author_id) VALUES(?, ?);";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, post.getText());
            stmt.setLong(2, post.getAuthorId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("Post doesn't create.");
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
        String sqlQuery = "SELECT * FROM posts WHERE post_id = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, postId);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("POST NOT FIND.");
            }
            return map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Post> findByAuthorId(long authorId) {
        String sqlQuery = "SELECT * FROM posts WHERE author_id = ?";

        List<Post> list = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, authorId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                list.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    @Override
    public void deleteById(long postId) {
        String sqlQuery = "DELETE FROM posts WHERE post_id = ?;";

        try (Connection connect = ConnectionManager.open();
             PreparedStatement stmt = connect.prepareStatement(sqlQuery)) {
            stmt.setLong(1, postId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("Post doesn't delete.");
            }
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
