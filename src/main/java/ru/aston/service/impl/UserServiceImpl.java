package ru.aston.service.impl;

import lombok.AllArgsConstructor;
import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.UserRepository;
import ru.aston.repository.impl.UserRepositoryImpl;
import ru.aston.service.UserService;

import java.util.List;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
        this.mapper = new UserMapper();
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.create(mapper.fromDto(userDto));
        return mapper.toDto(user);
    }

    @Override
    public UserDto findById(long userId) {
        User user = userRepository.findById(userId);
        return mapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.update(mapper.fromDto(userDto));
        return mapper.toDto(user);
    }

    @Override
    public boolean deleteById(long userId) {
        return userRepository.deleteById(userId);
    }

}
