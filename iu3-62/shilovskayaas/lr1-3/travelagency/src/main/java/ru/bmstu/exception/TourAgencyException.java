package ru.bmstu.exception;

//род класс исключений
public class TourAgencyException extends RuntimeException {
    public TourAgencyException(String message) {
        super(message);
    }
    public TourAgencyException(String message, Throwable cause) {
        super(message, cause);
    }
}

