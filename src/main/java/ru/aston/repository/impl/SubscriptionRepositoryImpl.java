package ru.aston.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.aston.entity.User;
import ru.aston.exception.CustomSqlException;
import ru.aston.repository.SubscriptionRepository;
import ru.aston.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.aston.util.RepositoryUtil.LOGIN;
import static ru.aston.util.RepositoryUtil.NAME;
import static ru.aston.util.RepositoryUtil.USER_ID;

@Slf4j
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    public static final String ADD_SQL_QUERY = "INSERT INTO subscriptions(user_id1, user_id2) VALUES(?, ?);";
    public static final String REMOVE_SQL_QUERY = "DELETE FROM subscriptions WHERE user_id1 = ? AND user_id2 = ?;";
    public static final String GET_SQL_QUERY = "SELECT * FROM users WHERE user_id IN (" +
            "SELECT user_id1 FROM subscriptions WHERE user_id2 = ?);";

    @Override
    public boolean add(long userId, long authorId) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(ADD_SQL_QUERY)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, authorId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public boolean remove(long userId, long authorId) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(REMOVE_SQL_QUERY)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, authorId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new CustomSqlException(e.getMessage());
        }
    }

    @Override
    public List<User> getSubscribers(long userId) {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_SQL_QUERY)) {
            stmt.setLong(1, userId);

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

    private User map(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong(USER_ID))
                .name(resultSet.getString(NAME))
                .login(resultSet.getString(LOGIN))
                .build();
    }

}
