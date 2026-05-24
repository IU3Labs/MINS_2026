package org.example.domain.model;

import org.example.domain.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Session {
    public static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final UUID id;
    private final Movie movie;
    private final Hall hall;
    private final LocalDateTime startTime;
    private final BigDecimal price;

    public Session(UUID id, Movie movie, Hall hall, LocalDateTime startTime, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price cannot be less that 0");
        }
        this.id = Objects.requireNonNull(id);
        this.movie = Objects.requireNonNull(movie);
        this.hall = Objects.requireNonNull(hall);
        this.startTime = Objects.requireNonNull(startTime);
        this.price = price;
    }

    public Session update(Movie movie, Hall hall, LocalDateTime startTime, BigDecimal price) {
        return new Session(id, movie, hall, startTime, price);
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(movie.getDurationMinutes());
    }

    public boolean supportsSeat(Seat seat) {
        return hall.isValidSeat(seat.getRow(), seat.getNumber());
    }

    public boolean overlapsWith(Session other) {
        if (!hall.getId().equals(other.hall.getId())) {
            return false;
        }
        return startTime.isBefore(other.getEndTime()) && other.startTime.isBefore(getEndTime());
    }

    public String getFormattedStartTime() {
        return startTime.format(DISPLAY_FORMATTER);
    }

    public UUID getId() { return id; }
    public Movie getMovie() { return movie; }
    public Hall getHall() { return hall; }
    public LocalDateTime getStartTime() { return startTime; }
    public BigDecimal getPrice() { return price; }
}
