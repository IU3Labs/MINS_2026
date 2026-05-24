package org.example.domain.model;

import org.example.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

public class Movie {
    private final UUID id;
    private final String title;
    private final int durationMinutes;
    private final String genre;
    private final String ageRating;

    public Movie(UUID id, String title, int durationMinutes, String genre, String ageRating) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Movie title cannot be empty");
        }
        if (durationMinutes <= 0) {
            throw new ValidationException("Movie duration should be more than 0 minutes");
        }
        this.id = Objects.requireNonNull(id);
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.ageRating = ageRating;
    }

    public Movie update(String title, int durationMinutes, String genre, String ageRating) {
        return new Movie(id, title, durationMinutes, genre, ageRating);
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getGenre() { return genre; }
    public String getAgeRating() { return ageRating; }
}
