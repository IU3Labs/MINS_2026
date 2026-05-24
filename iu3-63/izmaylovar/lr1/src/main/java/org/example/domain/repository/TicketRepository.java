package org.example.domain.repository;

import org.example.domain.model.Seat;
import org.example.domain.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {
    void save(Ticket ticket);
    void delete(Ticket ticket);
    void deleteBySessionId(UUID sessionId);
    Optional<Ticket> findById(UUID id);
    List<Ticket> findAll();
    List<Ticket> findBySessionId(UUID sessionId);
    Optional<Ticket> findBySessionIdAndSeat(UUID sessionId, Seat seat);
}
