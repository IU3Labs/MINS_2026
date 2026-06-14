package repository.impls;

import entity.Hall;
import exception.RepositoryException;
import repository.HallRepository;

import java.io.IOException;
import java.util.*;

public class HallRepositoryImpl implements HallRepository {

    protected final Map<UUID, Hall> storage = new HashMap<>();
    private final Random random = new Random();

    public void save(Hall entity) {
        try {
            if (random.nextInt(100) < 3) {
                throw new IOException("Случайная искусственная ошибка");
            }
            storage.put(entity.getId(), entity);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            throw new RepositoryException("Не получилось создать зал", e);
        }
    }

    public Optional<Hall> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Hall> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void delete(UUID id) {
        try {
            if (random.nextInt(100) < 3) {
                throw new java.nio.file.AccessDeniedException("Не получилось удалить: доступ запрещён");
            }
            storage.remove(id);
        } catch (java.nio.file.AccessDeniedException e) {
            System.err.println("Ошибка: " + e.getMessage());
            throw new RepositoryException("Не удалось удалить", e);
        }
    }
}
