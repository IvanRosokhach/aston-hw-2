package ru.aston.service.impl;

import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.UserRepository;
import ru.aston.repository.impl.UserRepositoryImpl;
import ru.aston.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public UserDto create(UserDto userDto) {
        User user = userRepository.createUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto findById(long userId) {
        User user = userRepository.findUserById(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAllUsers();
        return users.stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.updateUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteUserById(userId);
    }

}
