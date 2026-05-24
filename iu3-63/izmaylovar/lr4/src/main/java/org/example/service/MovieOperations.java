package org.example.service;

import org.example.domain.model.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieOperations {
    List<Movie> getAllMovies();

    Movie getMovie(UUID id);

    Movie createMovie(String title, int durationMinutes, String genre, String ageRating);

    Movie updateMovie(UUID id, String title, int durationMinutes, String genre, String ageRating);

    void deleteMovie(UUID id);
}
