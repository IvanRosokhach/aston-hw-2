package ru.aston.service;

import ru.aston.dto.UserDto;

import java.util.List;

public interface UserService extends BaseService<UserDto> {

    List<UserDto> findAll();

}
