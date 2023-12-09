package ru.aston.service.impl;

import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.SubscriptionRepository;
import ru.aston.repository.impl.SubscriptionRepositoryImpl;
import ru.aston.service.SubscriptionService;

import java.util.List;

public class SubscriptionServiceImpl implements SubscriptionService {

    SubscriptionRepository subscriptionRepository = new SubscriptionRepositoryImpl();

    @Override
    public List<UserDto> getSubscribers(long userId) {
        List<User> subscribers = subscriptionRepository.getSubscribers(userId);
        return subscribers.stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public void add(long userId, long authorId) {
        subscriptionRepository.add(userId, authorId);
    }

    @Override
    public void remove(long userId, long authorId) {
        subscriptionRepository.remove(userId, authorId);
    }

}
