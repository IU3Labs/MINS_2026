package org.example.domain.exception;

public class SeatAlreadyBookedException extends CinemaException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}
