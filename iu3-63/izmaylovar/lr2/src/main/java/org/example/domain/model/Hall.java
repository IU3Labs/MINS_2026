package org.example.domain.model;

import org.example.domain.exception.ValidationException;

import java.util.Objects;
import java.util.UUID;

public class Hall {
    private final UUID id;
    private final String name;
    private final int rows;
    private final int seatsPerRow;

    public Hall(UUID id, String name, int rows, int seatsPerRow) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Hall name cannot be empty");
        }
        if (rows <= 0 || seatsPerRow <= 0) {
            throw new ValidationException("Size of the hall should be more than 0");
        }
        this.id = Objects.requireNonNull(id);
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public boolean isValidSeat(int row, int seatNumber) {
        return row >= 1 && row <= rows && seatNumber >= 1 && seatNumber <= seatsPerRow;
    }

    public Hall update(String name, int rows, int seatsPerRow) {
        return new Hall(id, name, rows, seatsPerRow);
    }

    public int getCapacity() {
        return rows * seatsPerRow;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getRows() { return rows; }
    public int getSeatsPerRow() { return seatsPerRow; }
}
