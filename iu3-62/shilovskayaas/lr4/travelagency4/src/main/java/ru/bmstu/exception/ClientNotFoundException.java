package ru.bmstu.exception;

public class ClientNotFoundException extends TourAgencyException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
