package ru.bmstu.exception;

public class InvalidPriceException extends TourAgencyException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
