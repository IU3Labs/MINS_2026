package com.cinema.reference.service;

import com.cinema.reference.entity.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieService extends Service<Movie> {
    Result<Void> create(String title, int durationMinutes, String genre, int ageRestriction);
    List<Movie> getAll();
    Optional<Movie> getById(UUID id);
    Result<Void> update(UUID id, String title, int durationMinutes, String genre, int ageRestriction);
    Result<Void> delete(UUID id);
}
