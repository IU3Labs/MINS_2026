package service.uow;

import exception.UnitOfWorkException;
import service.CrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnitOfWork<T> implements IUnitOfWork<T> {
    private final CrudService<T> crudService;
    private final List<T> newEntities = new ArrayList<>();
    private final List<T> dirtyEntities = new ArrayList<>();
    private final List<UUID> removedIds = new ArrayList<>();

    public UnitOfWork(CrudService<T> crudService) {
        this.crudService = crudService;
    }

    @Override
    public void registerNew(T entity) {
        newEntities.add(entity);
    }

    @Override
    public void registerDirty(T entity) {
        if (!newEntities.contains(entity) && !dirtyEntities.contains(entity)) {
            dirtyEntities.add(entity);
        }
    }

    @Override
    public void registerRemoved(T entity, UUID id) {
        newEntities.remove(entity);
        dirtyEntities.remove(entity);
        removedIds.add(id);
    }

    @Override
    public void commit() {
        try {
            crudService.beginTransaction();
            for (T entity : newEntities) {
                crudService.create(entity);
            }
            for (T entity : dirtyEntities) {
                crudService.update(entity);
            }
            for (UUID id : removedIds) {
                crudService.delete(id);
            }
            crudService.commitTransaction();
        } catch (Exception e) {
            crudService.rollbackTransaction();
            throw new UnitOfWorkException("Ошибка при сохранении данных, изменения откатаны", e);
        } finally {
            clear();
        }
    }

    // todo строки в сервис справочников (ЛР4)
    @Override
    public void rollback() {
        crudService.rollbackTransaction();
        clear();
    }

    private void clear() {
        newEntities.clear();
        dirtyEntities.clear();
        removedIds.clear();
    }
}