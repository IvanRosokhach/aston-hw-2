package ru.aston.repository;

import ru.aston.entity.User;

import java.util.List;

public interface SubscriptionRepository {

    void add(long userId, long authorId);

    void remove(long userId, long authorId);

    List<User> getSubscribers(long userId);

}
