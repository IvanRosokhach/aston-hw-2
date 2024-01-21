package ru.aston.service.impl;

import lombok.AllArgsConstructor;
import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.SubscriptionRepository;
import ru.aston.repository.impl.SubscriptionRepositoryImpl;
import ru.aston.service.SubscriptionService;

import java.util.List;

@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserMapper mapper;

    public SubscriptionServiceImpl() {
        this.subscriptionRepository = new SubscriptionRepositoryImpl();
        this.mapper = new UserMapper();
    }

    @Override
    public List<UserDto> getSubscribers(long userId) {
        List<User> subscribers = subscriptionRepository.getSubscribers(userId);
        return subscribers.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public boolean add(long userId, long authorId) {
        return subscriptionRepository.add(userId, authorId);
    }

    @Override
    public boolean remove(long userId, long authorId) {
        return subscriptionRepository.remove(userId, authorId);
    }

}
