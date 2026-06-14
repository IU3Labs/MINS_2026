package entity;

import java.time.Duration;
import java.util.UUID;

public class Movie {
    private final UUID id;
    private final String title;
    private final Duration duration;
    private final String genre;
    private final int ageRestriction;

    public Movie(UUID id, String title, Duration duration, String genre, int ageRestriction) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.ageRestriction = ageRestriction;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public Duration getDuration() { return duration; }
    public String getGenre() { return genre; }
    public int getAgeRestriction() { return ageRestriction; }
}