package ru.aston.repository;

import ru.aston.entity.User;

import java.util.List;

public interface UserRepository extends BaseRepository<User> {

    List<User> findAll();

}
