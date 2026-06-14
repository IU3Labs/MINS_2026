package repository.impl;

import exception.CarNotFoundException;
import model.Car;
import model.Client;
import repository.CarRepository;

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