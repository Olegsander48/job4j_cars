package ru.job4j.cars.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    T create(T entity);

    void update(T entity);

    void delete(int id);

    Optional<T> findById(int id);

    List<T> findAllOrderById();
}
