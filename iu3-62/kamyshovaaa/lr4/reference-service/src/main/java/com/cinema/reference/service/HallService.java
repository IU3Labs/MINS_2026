package com.cinema.reference.service;

import com.cinema.reference.entity.Hall;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HallService extends Service<Hall> {
    Result<Void> create(String name, int rows, int seatsPerRow);
    List<Hall> getAll();
    Optional<Hall> getById(UUID id);
    Result<Void> update(UUID id, String name, int rows, int seatsPerRow);
    Result<Void> delete(UUID id);
}
