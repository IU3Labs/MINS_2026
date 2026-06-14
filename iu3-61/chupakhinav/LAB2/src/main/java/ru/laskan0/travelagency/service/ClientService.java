package ru.laskan0.travelagency.service;

import java.util.List;

import ru.laskan0.travelagency.exception.DuplicateEntityException;
import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.exception.NotFoundException;
import ru.laskan0.travelagency.factory.ClientFactory;
import ru.laskan0.travelagency.factory.DefaultClientFactory;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.repository.ClientRepository;
import ru.laskan0.travelagency.service.observer.TravelAgencyObserver;
import ru.laskan0.travelagency.util.IdGenerator;

public class ClientService {
    private static final String CLIENT_ID_PREFIX = "C-";

    private final ClientRepository clientRepository;
    private final ClientFactory clientFactory;
    private final List<TravelAgencyObserver> observers;

    public ClientService(ClientRepository clientRepository) {
        this(clientRepository, new DefaultClientFactory(), List.of());
    }

    public ClientService(ClientRepository clientRepository, ClientFactory clientFactory) {
        this(clientRepository, clientFactory, List.of());
    }

    public ClientService(
            ClientRepository clientRepository,
            ClientFactory clientFactory,
            List<TravelAgencyObserver> observers) {
        if (clientRepository == null) {
            throw new InvalidDataException("Репозиторий клиентов не может быть null");
        }
        if (clientFactory == null) {
            throw new InvalidDataException("Фабрика клиентов не может быть null");
        }
        if (observers == null) {
            throw new InvalidDataException("Список наблюдателей не может быть null");
        }
        this.clientRepository = clientRepository;
        this.clientFactory = clientFactory;
        this.observers = List.copyOf(observers);
    }

    public Client addClient(String fullName, String phone, Discount discount) {
        String clientId = generateNextClientId();
        Client client = clientFactory.create(clientId, fullName, phone, discount);
        return addClient(client);
    }

    public Client addClient(Client client) {
        if (client == null) {
            throw new InvalidDataException("Клиент для сохранения не может быть null");
        }
        if (clientRepository.existsById(client.getId())) {
            throw new DuplicateEntityException("Клиент с таким ID уже существует: " + client.getId());
        }
        Client savedClient = clientRepository.save(client);
        notifyClientAdded(savedClient);
        return savedClient;
    }

    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Клиент не найден: " + clientId));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    private void notifyClientAdded(Client client) {
        for (TravelAgencyObserver observer : observers) {
            observer.onClientAdded(client);
        }
    }

    private String generateNextClientId() {
        return IdGenerator.nextId(
                clientRepository.findAll().stream().map(Client::getId).toList(),
                CLIENT_ID_PREFIX);
    }
}
