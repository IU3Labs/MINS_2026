package ru.mins.parking.core.exception;

public class ReferenceServiceUnavailableException extends ParkingException {
    public ReferenceServiceUnavailableException() {
        super("Reference Service unavailable");
    }
}
