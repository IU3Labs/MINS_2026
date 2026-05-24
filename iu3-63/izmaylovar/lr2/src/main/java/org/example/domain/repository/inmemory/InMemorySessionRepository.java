package org.example.domain.repository.inmemory;

import org.example.domain.model.Session;
import org.example.domain.repository.SessionRepository;

import java.util.*;

public class InMemorySessionRepository implements SessionRepository {
    private final Map<UUID, Session> storage = new HashMap<>();

    @Override
    public void save(Session session) {
        storage.put(session.getId(), session);
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Session> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Session session) {
        storage.remove(session.getId());
    }
}
