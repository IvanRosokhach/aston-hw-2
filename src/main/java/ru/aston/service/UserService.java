package ru.aston.service;

import ru.aston.dto.UserDto;
import ru.aston.entity.User;
import ru.aston.mapper.UserMapper;
import ru.aston.repository.UserRepositories;

import java.util.List;

public class UserService {

    UserRepositories userRepositories = new UserRepositories();

    public UserDto create(UserDto userDto) {
        User user = userRepositories.createUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public UserDto findById(long id) {
        User user = userRepositories.findUserById(id);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> findAll() {
        List<User> allUsers = userRepositories.findAllUsers();
        return allUsers.stream().map(UserMapper::toUserDto).toList();
    }

    public UserDto update(UserDto userDto) {
        User user = userRepositories.updateUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public void delete(long id) {
        userRepositories.deleteUserById(id);
    }

}
