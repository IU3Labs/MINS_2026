package com.cinema.reference.service.impls;

import com.cinema.reference.repository.Repository;
import com.cinema.reference.service.CrudService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CrudServiceImpl<T> implements CrudService<T> {

    private final Repository<T> repository;

    public CrudServiceImpl(Repository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void beginTransaction() {
        repository.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        repository.commitTransaction();
    }

    @Override
    public void rollbackTransaction() {
        repository.rollbackTransaction();
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
