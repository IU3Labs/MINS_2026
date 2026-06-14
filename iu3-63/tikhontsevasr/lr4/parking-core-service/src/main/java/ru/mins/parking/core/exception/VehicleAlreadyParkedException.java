package ru.mins.parking.core.exception;

public class VehicleAlreadyParkedException extends ParkingException {
    public VehicleAlreadyParkedException(String message) {
        super(message);
    }
}
