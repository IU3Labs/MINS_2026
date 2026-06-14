package com.cinema.reference.service.uow;

import com.cinema.reference.service.CrudService;

public interface IUnitOfWorkFactory {
    <T> IUnitOfWork<T> create(CrudService<T> crudService);
}