package repository.impls;

import entity.Screening;
import exception.RepositoryException;
import repository.ScreeningRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ScreeningRepositoryImpl implements ScreeningRepository {

    protected final Map<UUID, Screening> storage = new HashMap<>();
    private Map<UUID, Screening> snapshot = null;
    private boolean inTransaction = false;
    private final Random random = new Random();

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

    public void save(Screening entity) {
        try {
            if (random.nextInt(100) < 3) {
                throw new IOException("Случайная искусственная ошибка");
            }
            storage.put(entity.getId(), entity);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            throw new RepositoryException("Не получилось создать сеанс", e);
        }
    }

    public Optional<Screening> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Screening> findAll() {
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

    @Override
    public List<Screening> findByDate(LocalDate date) {
        return storage.values().stream()
                .filter(screening -> screening.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
}