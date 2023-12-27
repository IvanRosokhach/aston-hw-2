package ru.aston.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.aston.entity.User;
import ru.aston.exception.CustomSqlException;
import ru.aston.exception.RepositoryException;
import ru.aston.repository.UserRepository;
import ru.aston.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.aston.util.RepositoryUtil.LOGIN;
import static ru.aston.util.RepositoryUtil.NAME;
import static ru.aston.util.RepositoryUtil.USER_ID;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    public static final String CREATE_SQL_QUERY = "INSERT INTO users(name, login) VALUES(?, ?);";
    public static final String FIND_BY_ID_SQL_QUERY = "SELECT * FROM users WHERE user_id = ?;";
    public static final String FIND_ALL_SQL_QUERY = "SELECT * FROM users;";
    public static final String UPDATE_SQL_QUERY = "UPDATE users SET name = ?, login = ? WHERE user_id = ?;";
    public static final String DELETE_SQL_QUERY = "DELETE FROM users WHERE user_id = ?;";

    @Override
    public User create(User user) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CREATE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows <= 0) {
                throw new RepositoryException("User doesn't create.");
            }
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                long id = result.getLong(1);
                user.setId(id);
            }
            return user;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public User findById(long userId) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_SQL_QUERY)) {
            stmt.setLong(1, userId);

            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                throw new RepositoryException("USER NOT FOUND");
            }
            return map(resultSet);

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_ALL_SQL_QUERY)) {

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                users.add(map(resultSet));
            }

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
        return users;
    }

    @Override
    public User update(User user) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setLong(3, user.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows <= 0) {
                throw new RepositoryException("User doesn't update.");
            }

            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                long id = result.getLong(1);
                user.setId(id);
            }
            return user;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public boolean deleteById(long userId) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_SQL_QUERY)) {
            stmt.setLong(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    private User map(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong(USER_ID))
                .name(resultSet.getString(NAME))
                .login(resultSet.getString(LOGIN))
                .build();
    }

}
