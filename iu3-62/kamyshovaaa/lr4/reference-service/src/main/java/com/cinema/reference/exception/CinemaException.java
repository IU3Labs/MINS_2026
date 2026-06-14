package com.cinema.reference.exception;

public abstract class CinemaException extends RuntimeException {
    public CinemaException(String message) {
        super(message);
    }
}
