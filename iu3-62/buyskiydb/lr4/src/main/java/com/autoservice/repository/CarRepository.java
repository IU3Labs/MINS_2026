package com.autoservice.repository;


import com.autoservice.exception.CarNotFoundException;
import com.autoservice.model.Car;

public interface CarRepository {
    void addCar(Car car);
    Car findCarByLicensePlate(String licensePlate) throws CarNotFoundException;

}