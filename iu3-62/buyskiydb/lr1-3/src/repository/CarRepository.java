package repository;

import exception.CarNotFoundException;
import model.Car;
import model.Client;

import java.util.List;

public interface CarRepository {
    void addCar(Car car);
    Car findCarByLicensePlate(String licensePlate) throws CarNotFoundException;

}