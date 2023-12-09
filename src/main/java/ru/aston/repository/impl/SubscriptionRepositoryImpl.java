package ru.aston.repository.impl;

import ru.aston.entity.User;
import ru.aston.repository.SubscriptionRepository;
import ru.aston.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    @Override
    public void add(long userId, long authorId) {
        String sqlQuery = "INSERT INTO subscriptions(user_id1, user_id2) VALUES(?, ?);";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, authorId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("Subscriptions doesn't create.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void remove(long userId, long authorId) {
        String sqlQuery = "DELETE FROM subscriptions WHERE user_id1 = ? AND user_id2 = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, authorId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows <= 0) {
                throw new RuntimeException("Subscriptions doesn't delete.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> getSubscribers(long userId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id IN(" +
                "SELECT user_id1 FROM subscriptions WHERE user_id2 = ?);";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, userId);

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

    private User map(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .build();
    }

}
