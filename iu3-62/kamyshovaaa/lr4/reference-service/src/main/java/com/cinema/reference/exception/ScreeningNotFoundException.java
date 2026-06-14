package com.cinema.reference.exception;

public class ScreeningNotFoundException extends CinemaException {
    public ScreeningNotFoundException(String id) {
        super("Сеанс не найден: " + id);
    }
}
