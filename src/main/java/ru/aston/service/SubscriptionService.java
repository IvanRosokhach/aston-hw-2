package ru.aston.service;

import ru.aston.dto.UserDto;

import java.util.List;

public interface SubscriptionService {

    List<UserDto> getSubscribers(long userId);

    boolean add(long userId, long authorId);

    boolean remove(long userId, long authorId);

}
