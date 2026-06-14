package ru.laskan0.travelagency.repository.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.repository.ClientRepository;

public class InMemoryClientRepository implements ClientRepository {
    private final Map<String, Client> clients = new LinkedHashMap<>();

    @Override
    public Client save(Client client) {
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public Optional<Client> findById(String id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public boolean existsById(String id) {
        return clients.containsKey(id);
    }
}
