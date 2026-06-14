package com.cinema.reference.service.uow;

import com.cinema.reference.service.CrudService;

public class UnitOfWorkFactory implements IUnitOfWorkFactory {

    @Override
    public <T> IUnitOfWork<T> create(CrudService<T> crudService) {
        return new UnitOfWork<>(crudService);
    }
}