package repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
    void save(T entity);
    Optional<T> findById(UUID id);
    List<T> findAll();
    void delete(UUID id);
}
