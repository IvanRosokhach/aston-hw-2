package ru.aston.service;

import ru.aston.dto.UserDto;

import java.util.List;

public interface SubscriptionService {

    List<UserDto> getSubscribers(long userId);

    void add(long userId, long authorId);

    void remove(long userId, long authorId);

}
