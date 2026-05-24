package org.example.domain.repository.inmemory;

import org.example.domain.model.Hall;
import org.example.domain.repository.HallRepository;

import java.util.*;

public class InMemoryHallRepository implements HallRepository {
    private final Map<UUID, Hall> storage = new HashMap<>();

    public void save(Hall hall) {
        storage.put(hall.getId(), hall);
    }

    public Optional<Hall> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Hall> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void delete(Hall hall) {
        storage.remove(hall.getId());
    }
}
