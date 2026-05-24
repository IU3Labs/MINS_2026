package org.example.service;

import org.example.domain.model.Hall;

import java.util.List;
import java.util.UUID;

public interface HallOperations {
    List<Hall> getAllHalls();

    Hall getHall(UUID id);

    Hall createHall(String name, int rows, int seatsPerRow);

    Hall updateHall(UUID id, String name, int rows, int seatsPerRow);

    void deleteHall(UUID id);
}
