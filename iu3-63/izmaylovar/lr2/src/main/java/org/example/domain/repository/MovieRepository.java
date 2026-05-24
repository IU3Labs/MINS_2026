package org.example.domain.repository;

import org.example.domain.model.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository {
    void save(Movie movie);
    void delete(Movie movie);
    Optional<Movie> findById(UUID id);
    List<Movie> findAll();
}
