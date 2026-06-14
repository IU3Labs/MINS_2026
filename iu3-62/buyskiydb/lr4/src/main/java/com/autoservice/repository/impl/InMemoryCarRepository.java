package com.autoservice.repository.impl;


import com.autoservice.exception.CarNotFoundException;
import com.autoservice.model.Car;
import com.autoservice.repository.CarRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryCarRepository implements CarRepository {
    private final Map<String, Car> carsByPlate = new HashMap<>();

    @Override
    public void addCar(Car car) {
        carsByPlate.put(car.getLicensePlate(), car);
    }

    @Override
    public Car findCarByLicensePlate(String licensePlate) throws CarNotFoundException {
        Car car = carsByPlate.get(licensePlate);
        if (car == null) {
            throw new CarNotFoundException(licensePlate);
        }
        return car;
    }

}