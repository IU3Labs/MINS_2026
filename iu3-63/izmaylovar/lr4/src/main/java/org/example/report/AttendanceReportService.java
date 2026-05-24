package org.example.report;

import org.example.domain.exception.SessionNotFoundException;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AttendanceReportService {
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    public AttendanceReportService(SessionRepository sessionRepository, TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
    }

    public AttendanceReport generateSessionReport(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        List<Ticket> tickets = ticketRepository.findBySessionId(sessionId);
        Map<Seat, Ticket> latestTicketsBySeat = new LinkedHashMap<>();
        for (Ticket ticket : tickets) {
            latestTicketsBySeat.put(ticket.getSeat(), ticket);
        }

        List<Ticket> actualTickets = latestTicketsBySeat.values().stream().toList();

        int reserved = (int) actualTickets.stream().filter(t -> t.getStatus() == TicketStatus.RESERVED).count();
        int purchased = (int) actualTickets.stream().filter(t -> t.getStatus() == TicketStatus.PURCHASED).count();
        int cancelled = (int) actualTickets.stream().filter(t -> t.getStatus() == TicketStatus.CANCELLED).count();

        int occupied = reserved + purchased;
        int capacity = session.getHall().getCapacity();
        int free = capacity - occupied;

        double occupancyPercent = capacity == 0 ? 0.0 : occupied * 100.0 / capacity;
        BigDecimal revenue = actualTickets.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.PURCHASED)
                .map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AttendanceReport(
                session.getId(),
                session.getMovie().getTitle(),
                session.getStartTime(),
                capacity,
                reserved,
                purchased,
                cancelled,
                occupied,
                free,
                occupancyPercent,
                revenue
        );
    }

    public List<AttendanceReport> generateAllSessionsReport() {
        return sessionRepository.findAll().stream()
                .sorted(Comparator.comparing(Session::getStartTime))
                .map(session -> generateSessionReport(session.getId()))
                .toList();
    }
}
