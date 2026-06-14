package ru.bmstu.repository;

import ru.bmstu.exception.TourAgencyException;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id) throws TourAgencyException;
}
