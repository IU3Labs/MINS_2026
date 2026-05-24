package org.example.domain.repository;

import org.example.domain.model.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {
    void save(Session session);
    void delete(Session session);
    Optional<Session> findById(UUID id);
    List<Session> findAll();
}
