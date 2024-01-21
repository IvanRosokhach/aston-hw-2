package ru.aston.service;

public interface BaseService<T> {

    T create(T entity);

    T findById(long id);

    T update(T entity);

    boolean deleteById(long id);

}
