package com.cinema.reference.exception;

public class MovieNotFoundException extends CinemaException {
    public MovieNotFoundException(String id) {
        super("Фильм не найден: " + id);
    }
}
