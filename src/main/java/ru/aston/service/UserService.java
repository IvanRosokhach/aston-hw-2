package ru.aston.service;

import ru.aston.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto findById(long userId);

    List<UserDto> findAll();

    UserDto update(UserDto userDto);

    void delete(long userId);

}
