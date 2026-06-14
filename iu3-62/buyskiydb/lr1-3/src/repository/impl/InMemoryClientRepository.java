package repository.impl;

import exception.ClientNotFoundException;
import model.Client;
import repository.ClientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryClientRepository implements ClientRepository {
    private final Map<String, Client> clientsByPhone = new HashMap<>();

    @Override
    public void addClient(Client client) {
        clientsByPhone.put(client.getPhone(), client);
    }

    @Override
    public Client findClientByPhone(String phone) throws ClientNotFoundException {
        Client client = clientsByPhone.get(phone);
        if (client == null) {
            throw new ClientNotFoundException(phone);
        }
        return client;
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(clientsByPhone.values());
    }
}