package service;

import exception.ClientNotFoundException;
import model.Car;
import model.Client;
import repository.CarRepository;
import repository.ClientRepository;

import java.util.List;

public class ClientManagementService {
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;

    public ClientManagementService(ClientRepository clientRepository, CarRepository carRepository) {
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
    }

    public void registerClient(String name, String phone, String email) {
        Client client = new Client(name, phone, email);
        clientRepository.addClient(client);
    }

    public void registerCar(String licensePlate, String brand, String model, int year, String ownerPhone)
            throws ClientNotFoundException {
        Client owner = clientRepository.findClientByPhone(ownerPhone);
        Car car = new Car(licensePlate, brand, model, year, owner);
        carRepository.addCar(car);
    }

    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }
}