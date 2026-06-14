package service.uow;

import service.CrudService;

public interface IUnitOfWorkFactory {
    <T> IUnitOfWork<T> create(CrudService<T> crudService);
}