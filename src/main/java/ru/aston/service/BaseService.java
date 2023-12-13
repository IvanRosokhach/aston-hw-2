package ru.aston.service;

public interface BaseService<T> {

    T create(T entity);

    T findById(long id);

    boolean deleteById(long id);

}
