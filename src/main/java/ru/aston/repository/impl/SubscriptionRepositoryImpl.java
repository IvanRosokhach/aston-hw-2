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

    public static final String ADD_SQL_QUERY = "INSERT INTO subscriptions(user_id1, user_id2) VALUES(?, ?);";
    public static final String REMOVE_SQL_QUERY = "DELETE FROM subscriptions WHERE user_id1 = ? AND user_id2 = ?;";
    public static final String GET_SQL_QUERY = "SELECT * FROM users WHERE user_id IN (" +
            "SELECT user_id1 FROM subscriptions WHERE user_id2 = ?);";

    @Override
    public boolean add(long userId, long authorId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(ADD_SQL_QUERY)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, authorId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean remove(long userId, long authorId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(REMOVE_SQL_QUERY)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, authorId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> getSubscribers(long userId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_SQL_QUERY)) {
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
