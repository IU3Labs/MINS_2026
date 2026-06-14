package service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudService<T> {
    void create(T entity);
    List<T> getAll();
    Optional<T> getById(UUID id);
    void update(T entity);
    void delete(UUID id);
}
