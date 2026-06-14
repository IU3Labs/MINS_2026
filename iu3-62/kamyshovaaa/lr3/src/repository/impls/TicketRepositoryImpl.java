package repository.impls;

import entity.Ticket;
import exception.RepositoryException;
import repository.TicketRepository;

import java.util.*;

public class TicketRepositoryImpl implements TicketRepository {

    protected final Map<UUID, Ticket> storage = new HashMap<>();
    private Map<UUID, Ticket> snapshot = null;
    private boolean inTransaction = false;

    @Override
    public void beginTransaction() {
        if (inTransaction) throw new IllegalStateException("Транзакция уже активна");
        snapshot = new HashMap<>(storage);
        inTransaction = true;
    }

    @Override
    public void commitTransaction() {
        if (!inTransaction) throw new IllegalStateException("Нет активной транзакции");
        snapshot = null;
        inTransaction = false;
    }

    @Override
    public void rollbackTransaction() {
        if (!inTransaction) return;
        storage.clear();
        storage.putAll(snapshot);
        snapshot = null;
        inTransaction = false;
    }

    @Override
    public void save(Ticket entity) {
        storage.put(entity.getId(), entity);
    }

    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void delete(UUID id) {
        storage.remove(id);
    }
}