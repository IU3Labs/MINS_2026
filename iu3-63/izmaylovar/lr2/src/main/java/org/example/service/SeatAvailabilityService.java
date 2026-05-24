package org.example.service;

import org.example.domain.exception.InvalidSeatException;
import org.example.domain.exception.SessionNotFoundException;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SeatAvailabilityService {
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    public SeatAvailabilityService(SessionRepository sessionRepository, TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
    }

    public void validateSeat(UUID sessionId, Seat seat) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        if (!session.supportsSeat(seat)) {
            throw new InvalidSeatException("There is no such seat in the selected hall: " + seat);
        }
    }

    public boolean isSeatAvailable(UUID sessionId, Seat seat) {
        validateSeat(sessionId, seat);

        Optional<Ticket> existing = ticketRepository.findBySessionIdAndSeat(sessionId, seat);
        if (existing.isEmpty()) {
            return true;
        }

        return existing.get().getStatus() == TicketStatus.CANCELLED;
    }

    public List<Seat> getAvailableSeats(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        List<Seat> result = new ArrayList<>();
        for (int row = 1; row <= session.getHall().getRows(); row++) {
            for (int number = 1; number <= session.getHall().getSeatsPerRow(); number++) {
                Seat seat = new Seat(row, number);
                if (isSeatAvailable(sessionId, seat)) {
                    result.add(seat);
                }
            }
        }
        return result;
    }
}
