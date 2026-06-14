package ru.bmstu.repository;

import ru.bmstu.exception.ClientNotFoundException;
import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Client;

import java.util.*;

public class ClientRepository implements Repository<Client, Integer> {
    private final Map<Integer, Client> clients = new HashMap<>();
    private int currentId = 1;

    @Override
    public Client save(Client client) {
        if (client.getId() == 0) {
            client.setId(currentId++);
            clients.put(client.getId(), client);
        } else {
            clients.put(client.getId(), client);
        }
        return client;
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<Client> findAll() {
        List<Client> sorted = new ArrayList<>(clients.values());
        sorted.sort(Comparator.comparingInt(Client::getId));
        return sorted;
    }

    @Override
    public void deleteById(Integer id) throws TourAgencyException {
        if (!clients.containsKey(id)) {
            throw new ClientNotFoundException("Клиент с ID " + id + " не найден.");
        }
        clients.remove(id);
    }

    public Optional<Client> findByEmail(String email) {
        return clients.values().stream()
                .filter(client -> client.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}