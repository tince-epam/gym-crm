package com.epam.gymcrm.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    T save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void deleteById(Long id);

    void update(T entity);
}
