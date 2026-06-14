package ru.laskan0.travelagency.repository;

import java.util.List;
import java.util.Optional;

import ru.laskan0.travelagency.model.Client;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(String id);
    List<Client> findAll();
    boolean existsById(String id);
}
