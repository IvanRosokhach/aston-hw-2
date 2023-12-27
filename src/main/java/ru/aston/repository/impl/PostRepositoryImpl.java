package ru.aston.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.aston.entity.Post;
import ru.aston.exception.CustomSqlException;
import ru.aston.exception.RepositoryException;
import ru.aston.repository.PostRepository;
import ru.aston.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.aston.util.RepositoryUtil.AUTHOR_ID;
import static ru.aston.util.RepositoryUtil.POST_ID;
import static ru.aston.util.RepositoryUtil.TEXT;

@Slf4j
public class PostRepositoryImpl implements PostRepository {

    public static final String CREATE_SQL_QUERY = "INSERT INTO posts(text, author_id) VALUES(?, ?);";
    public static final String FIND_BY_ID_SQL_QUERY = "SELECT * FROM posts WHERE post_id = ?;";
    public static final String FIND_BY_AUTHOR_ID_SQL_QUERY = "SELECT * FROM posts WHERE author_id = ?";
    public static final String UPDATE_SQL_QUERY = "UPDATE posts SET text = ? WHERE post_id = ?;";
    public static final String DELETE_BY_ID_SQL_QUERY = "DELETE FROM posts WHERE post_id = ?;";

    @Override
    public Post create(Post post) {
        try (Connection connection = ConnectionPool.getConnection();
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
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
        return post;
    }

    @Override
    public Post findById(long postId) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_SQL_QUERY)) {
            stmt.setLong(1, postId);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                throw new RepositoryException("POST NOT FOUND.");
            }
            return map(resultSet);

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public List<Post> findByAuthorId(long authorId) {
        List<Post> list = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_AUTHOR_ID_SQL_QUERY)) {
            stmt.setLong(1, authorId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                list.add(map(resultSet));
            }

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
        return list;
    }

    @Override
    public Post update(Post post) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, post.getText());
            stmt.setLong(2, post.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows <= 0) {
                throw new RepositoryException("Post doesn't update.");
            }

            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                long id = result.getLong(1);
                post.setId(id);
            }
            return post;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public boolean deleteById(long postId) {
        try (Connection connect = ConnectionPool.getConnection();
             PreparedStatement stmt = connect.prepareStatement(DELETE_BY_ID_SQL_QUERY)) {
            stmt.setLong(1, postId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    private Post map(ResultSet resultSet) throws SQLException {
        return Post.builder()
                .id(resultSet.getLong(POST_ID))
                .text(resultSet.getString(TEXT))
                .authorId(resultSet.getLong(AUTHOR_ID))
                .build();
    }

}
