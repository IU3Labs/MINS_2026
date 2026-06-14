package repository.impls;

import entity.Ticket;
import exception.RepositoryException;
import repository.TicketRepository;

import java.io.IOException;
import java.util.*;

public class TicketRepositoryImpl implements TicketRepository {

    protected final Map<UUID, Ticket> storage = new HashMap<>();
    private final Random random = new Random();

    @Override
    public void save(Ticket entity) {
        try {
            if (random.nextInt(100) < 10) {
                throw new IOException("Случайная искусственная ошибка");
            }
            storage.put(entity.getId(), entity);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            throw new RepositoryException("Не получилось создать билет", e);
        }
    }

    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void delete(UUID id) {
        try {
            if (random.nextInt(100) < 10) {
                throw new java.nio.file.AccessDeniedException("Не получилось удалить: доступ запрещён");
            }
            storage.remove(id);
        } catch (java.nio.file.AccessDeniedException e) {
            System.err.println("Ошибка: " + e.getMessage());
            throw new RepositoryException("Не удалось удалить", e);
        }
    }
}