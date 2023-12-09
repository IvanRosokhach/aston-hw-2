package ru.aston.repository;

import ru.aston.entity.User;

import java.util.List;

public interface UserRepository {

    User createUser(User user);

    User findUserById(long userId);

    List<User> findAllUsers();

    User updateUser(User user);

    void deleteUserById(long userId);

}
