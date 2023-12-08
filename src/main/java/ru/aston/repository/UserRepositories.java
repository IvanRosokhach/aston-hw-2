package ru.aston.repository;

import ru.aston.entity.User;
import ru.aston.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositories {

    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users(name, login) VALUES(?, ?);";

        long id = 0;

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
                id = result.getLong(1);
                user.setId(id);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return user;
    }

    public User findUserById(long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id=?;";
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("NOT FIND USER");
            }
            return map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAllUsers() {
        String sqlQuery = "SELECT * FROM users;";
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(map(resultSet));
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUser(User user) {

        String sqlQuery = "UPDATE users SET name=?, login=? WHERE user_id=?;";

        long id = 0;

        try (Connection connect = ConnectionManager.open();
             PreparedStatement stmt = connect.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setLong(3, user.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("User doesn't update.");
            }
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                id = result.getLong(1);
                user.setId(id);
                return user;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return user;
    }

    public void deleteUserById(Long userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id=?;";

        try (Connection connect = ConnectionManager.open();
             PreparedStatement stmt = connect.prepareStatement(sqlQuery)) {

            stmt.setLong(1, userId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("User doesn't delete.");
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public User map(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .build();
    }

}
