package ru.aston.repository;

public interface BaseRepository<T> {

    T create(T entity);

    T findById(long id);

    T update(T entity);

    boolean deleteById(long id);

}
