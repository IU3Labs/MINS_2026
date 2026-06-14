package ru.bmstu.travel.core.exception;

public class NotFoundException extends TravelAgencyException {
    public NotFoundException(String message) {
        super(message);
    }
}
