package ru.shpi0.imageserver.repository;

public interface CommonRepository<T> {

    void save (T t);

    void delete(T t);

    void update(T t);

}
