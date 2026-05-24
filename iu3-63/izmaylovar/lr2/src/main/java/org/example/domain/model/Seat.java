package org.example.domain.model;

import org.example.domain.exception.ValidationException;

import java.util.Objects;

public class Seat {
    private final int row;
    private final int number;

    public Seat(int row, int number) {
        if (row <= 0 || number <= 0) {
            throw new ValidationException("Row and number should be more than 0");
        }
        this.row = row;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat seat)) return false;
        return row == seat.row && number == seat.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }

    @Override
    public String toString() {
        return "row=" + row + ", seat=" + number;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }
}
