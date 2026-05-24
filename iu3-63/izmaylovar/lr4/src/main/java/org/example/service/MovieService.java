package org.example.service;

import org.example.domain.exception.EntityInUseException;
import org.example.domain.exception.NotFoundException;
import org.example.domain.model.Movie;
import org.example.domain.repository.MovieRepository;
import org.example.domain.repository.SessionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MovieService implements MovieOperations {
    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;

    public MovieService(MovieRepository movieRepository, SessionRepository sessionRepository) {
        this.movieRepository = movieRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll().stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .toList();
    }

    public Movie getMovie(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found: " + id));
    }

    public Movie createMovie(String title, int durationMinutes, String genre, String ageRating) {
        Movie movie = new Movie(UUID.randomUUID(), title, durationMinutes, genre, ageRating);
        movieRepository.save(movie);
        return movie;
    }

    public Movie updateMovie(UUID id, String title, int durationMinutes, String genre, String ageRating) {
        Movie updatedMovie = getMovie(id).update(title, durationMinutes, genre, ageRating);
        movieRepository.save(updatedMovie);
        return updatedMovie;
    }

    public void deleteMovie(UUID id) {
        Movie movie = getMovie(id);
        boolean inUse = sessionRepository.findAll().stream()
                .anyMatch(session -> session.getMovie().getId().equals(id));
        if (inUse) {
            throw new EntityInUseException("Movie is used in existing sessions and cannot be removed.");
        }
        movieRepository.delete(movie);
    }
}
