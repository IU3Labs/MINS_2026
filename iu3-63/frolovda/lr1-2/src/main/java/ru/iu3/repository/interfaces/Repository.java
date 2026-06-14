package ru.iu3.repository.interfaces;

import java.util.List;

public interface Repository<T, ID> {
    void add(T entity);

    T getById(int ID);
    
    List<T> getAll();

    void update(T entity);
}
