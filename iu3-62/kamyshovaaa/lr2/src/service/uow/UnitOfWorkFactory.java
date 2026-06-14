package service.uow;

import service.CrudService;

public class UnitOfWorkFactory implements IUnitOfWorkFactory {

    @Override
    public <T> IUnitOfWork<T> create(CrudService<T> crudService) {
        return new UnitOfWork<>(crudService);
    }
}