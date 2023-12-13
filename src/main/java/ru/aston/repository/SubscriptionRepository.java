package ru.aston.repository;

import ru.aston.entity.User;

import java.util.List;

public interface SubscriptionRepository {

    boolean add(long userId, long authorId);

    boolean remove(long userId, long authorId);

    List<User> getSubscribers(long userId);

}
