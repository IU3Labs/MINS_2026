package org.example.domain.repository.inmemory;

import org.example.domain.model.Movie;
import org.example.domain.repository.MovieRepository;

import java.util.*;

public class InMemoryMovieRepository implements MovieRepository {
    private final Map<UUID, Movie> storage = new HashMap<>();

    @Override
    public void save(Movie movie) {
        storage.put(movie.getId(), movie);
    }

    @Override
    public void delete(Movie movie) {
        storage.remove(movie.getId());
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(storage.values());
    }

}
