package ru.bmstu.exception;

public class InvalidDiscountException extends TourAgencyException {
    public InvalidDiscountException(String message) {
        super(message);
    }
}
