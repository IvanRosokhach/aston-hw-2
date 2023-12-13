package ru.aston.repository;

public interface BaseRepository<T> {

    T create(T entity);

    T findById(long id);

    boolean deleteById(long id);

}
