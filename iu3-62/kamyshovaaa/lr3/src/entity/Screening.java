package entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Screening {
    private final UUID id;
    private final Movie movie;
    private final Hall hall;
    private final LocalDateTime startTime;
    private final double ticketPrice;

    public Screening(UUID id, Movie movie, Hall hall, LocalDateTime startTime, double ticketPrice) {
        this.id = id;
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.ticketPrice = ticketPrice;
    }

    public UUID getId() { return id; }
    public Movie getMovie() { return movie; }
    public Hall getHall() { return hall; }
    public LocalDateTime getStartTime() { return startTime; }
    public double getTicketPrice() { return ticketPrice; }

    public String getMovieTitle() {
        return movie.getTitle();
    }
    
    public String getHallName() {
        return hall.getName();
    }
}