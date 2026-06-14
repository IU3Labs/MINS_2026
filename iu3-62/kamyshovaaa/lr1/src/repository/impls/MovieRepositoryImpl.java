package repository.impls;

import entity.Movie;
import exception.RepositoryException;
import exception.ValidationException;
import repository.MovieRepository;

import java.io.IOException;
import java.util.*;

public class MovieRepositoryImpl implements MovieRepository {

    protected final Map<UUID, Movie> storage = new HashMap<>();
    private final Random random = new Random();

    public void save(Movie entity) {
        try {
            if (random.nextInt(100) < 10) {
                throw new IOException("Случайная искусственная ошибка");
            }
            storage.put(entity.getId(), entity);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            throw new RepositoryException("Не получилось создать фильм", e);
        }
    }

    public Optional<Movie> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Movie> findAll() {
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