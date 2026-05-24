package org.example.service;

import org.example.domain.exception.EntityInUseException;
import org.example.domain.exception.NotFoundException;
import org.example.domain.model.Hall;
import org.example.domain.repository.HallRepository;
import org.example.domain.repository.SessionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class HallService implements HallOperations {
    private final HallRepository hallRepository;
    private final SessionRepository sessionRepository;

    public HallService(HallRepository hallRepository, SessionRepository sessionRepository) {
        this.hallRepository = hallRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll().stream()
                .sorted(Comparator.comparing(Hall::getName))
                .toList();
    }

    public Hall getHall(UUID id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hall not found: " + id));
    }

    public Hall createHall(String name, int rows, int seatsPerRow) {
        Hall hall = new Hall(UUID.randomUUID(), name, rows, seatsPerRow);
        hallRepository.save(hall);
        return hall;
    }

    public Hall updateHall(UUID id, String name, int rows, int seatsPerRow) {
        Hall updatedHall = getHall(id).update(name, rows, seatsPerRow);
        hallRepository.save(updatedHall);
        return updatedHall;
    }

    public void deleteHall(UUID id) {
        Hall hall = getHall(id);
        boolean inUse = sessionRepository.findAll().stream()
                .anyMatch(session -> session.getHall().getId().equals(id));
        if (inUse) {
            throw new EntityInUseException("Hall is used in existing sessions and cannot be removed.");
        }
        hallRepository.delete(hall);
    }
}
