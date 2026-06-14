package com.cinema.reference.entity;

import java.util.UUID;

public class Hall {
    private final UUID id;
    private String name;
    private int rows;
    private int seatsPerRow;

    public Hall(UUID id, String name, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getRows() { return rows; }
    public int getSeatsPerRow() { return seatsPerRow; }
}