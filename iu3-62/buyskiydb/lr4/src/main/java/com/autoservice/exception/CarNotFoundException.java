package com.autoservice.exception;

public class CarNotFoundException extends AutoServiceException {
    public CarNotFoundException(String licensePlate) {
        super("Автомобиль с номером " + licensePlate + " не найден");
    }
}