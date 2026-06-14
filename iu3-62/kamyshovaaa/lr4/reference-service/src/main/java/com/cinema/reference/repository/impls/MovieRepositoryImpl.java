package com.cinema.reference.repository.impls;

import com.cinema.reference.entity.Movie;
import com.cinema.reference.repository.MovieRepository;
import com.cinema.reference.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MovieRepositoryImpl implements MovieRepository, Repository<Movie> {
    private static final Logger log = LoggerFactory.getLogger(MovieRepositoryImpl.class);

    private final Map<UUID, Movie> movies = new HashMap<>();
    private Map<UUID, Movie> transactionSnapshot = null;
    private boolean inTransaction = false;

    // ========== СТАРЫЕ МЕТОДЫ (сохраняем без изменений) ==========

    @Override
    public void save(Movie movie) {
        movies.put(movie.getId(), movie);
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return Optional.ofNullable(movies.get(id));
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(movies.values());
    }

    @Override
    public void delete(UUID id) {
        movies.remove(id);
    }

    // ========== НОВЫЕ МЕТОДЫ (из Repository<Movie>) ==========

    @Override
    public void beginTransaction() {
        if (inTransaction) {
            return;
        }
        // Делаем снимок текущего состояния
        transactionSnapshot = new HashMap<>(movies);
        inTransaction = true;
    }

    @Override
    public void commitTransaction() {
        if (!inTransaction) {
            return;
        }
        // Принимаем изменения — просто очищаем снимок
        transactionSnapshot = null;
        inTransaction = false;
    }

    @Override
    public void rollbackTransaction() {
        if (!inTransaction) {
            return;
        }
        // Восстанавливаем состояние из снимка
        if (transactionSnapshot != null) {
            movies.clear();
            movies.putAll(transactionSnapshot);
            transactionSnapshot = null;
        }
        inTransaction = false;
    }
}