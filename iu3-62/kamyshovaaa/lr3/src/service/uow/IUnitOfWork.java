package service.uow;

import java.util.UUID;

public interface IUnitOfWork<T> {
    void registerNew(T entity);
    void registerDirty(T entity);
    void registerRemoved(T entity, UUID id);
    void commit();
    void rollback();
}