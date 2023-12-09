package ru.aston.repository.impl;

import ru.aston.entity.User;
import ru.aston.repository.UserRepository;
import ru.aston.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users(name, login) VALUES(?, ?);";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows <= 0) {
                throw new RuntimeException("User doesn't create.");
            }
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                long id = result.getLong(1);
                user.setId(id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    @Override
    public User findUserById(long userId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("USER NOT FIND");
            }
            return map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> findAllUsers() {
        String sqlQuery = "SELECT * FROM users;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = stmt.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(map(resultSet));
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET name = ?, login = ? WHERE user_id = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setLong(3, user.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("User doesn't update.");
            }

            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                long id = result.getLong(1);
                user.setId(id);
                return user;
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    @Override
    public void deleteUserById(long userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, userId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("User doesn't delete.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private User map(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .build();
    }

}
