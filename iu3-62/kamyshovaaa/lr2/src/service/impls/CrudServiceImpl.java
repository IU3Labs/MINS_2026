package service.impls;

import repository.Repository;
import service.CrudService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CrudServiceImpl<T> implements CrudService<T> {

    private final Repository<T> repository;

    public CrudServiceImpl(Repository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void create(T entity) {
        repository.save(entity);
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public void update(T entity) {
        repository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        repository.delete(id);
    }
}
