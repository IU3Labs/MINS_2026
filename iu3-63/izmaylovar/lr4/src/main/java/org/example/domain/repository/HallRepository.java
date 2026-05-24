package org.example.domain.repository;

import org.example.domain.model.Hall;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HallRepository {
    void save(Hall hall);
    void delete(Hall hall);
    Optional<Hall> findById(UUID id);
    List<Hall> findAll();
}
