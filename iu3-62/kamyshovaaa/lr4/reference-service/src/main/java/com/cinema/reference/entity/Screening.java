package com.cinema.reference.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Screening {
    private final UUID id;
    private Movie movie;
    private Hall hall;
    private LocalDateTime startTime;
    private double ticketPrice;

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
}