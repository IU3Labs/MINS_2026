package ru.bmstu.exception;

public class TourNotFoundException extends TourAgencyException {
    public TourNotFoundException(String message) {
        super(message);
    }
}
