package org.example.domain.repository.inmemory;

import org.example.domain.model.Seat;
import org.example.domain.model.Ticket;
import org.example.domain.repository.TicketRepository;

import java.util.*;

public class InMemoryTicketRepository implements TicketRepository {
    private final Map<UUID, Ticket> storage = new HashMap<>();

    @Override
    public void save(Ticket ticket) {
        storage.put(ticket.getId(), ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        storage.remove(ticket.getId());
    }

    @Override
    public void deleteBySessionId(UUID sessionId) {
        storage.values().removeIf(ticket -> ticket.getSessionId().equals(sessionId));
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Ticket::getCreatedAt))
                .toList();
    }

    @Override
    public List<Ticket> findBySessionId(UUID sessionId) {
        return storage.values().stream()
                .filter(t -> t.getSessionId().equals(sessionId))
                .sorted(Comparator.comparing(Ticket::getCreatedAt))
                .toList();
    }

    @Override
    public Optional<Ticket> findBySessionIdAndSeat(UUID sessionId, Seat seat) {
        return storage.values().stream()
                .filter(x -> x.getSessionId().equals(sessionId))
                .filter(t -> t.getSeat().equals(seat))
                .max(Comparator.comparing(Ticket::getCreatedAt));
    }
}
