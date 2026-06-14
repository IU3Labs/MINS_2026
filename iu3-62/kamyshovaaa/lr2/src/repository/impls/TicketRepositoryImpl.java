package repository.impls;

import entity.Ticket;
import exception.RepositoryException;
import repository.TicketRepository;

import java.util.*;

public class TicketRepositoryImpl implements TicketRepository {

    protected final Map<UUID, Ticket> storage = new HashMap<>();

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