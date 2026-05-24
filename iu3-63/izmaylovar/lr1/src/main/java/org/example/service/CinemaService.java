package org.example.service;

import org.example.domain.exception.SeatAlreadyBookedException;
import org.example.domain.exception.SessionNotFoundException;
import org.example.domain.exception.TicketNotFoundException;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.service.factory.TicketFactoryRegistry;
import org.example.service.subscription.SessionSubscriptionService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CinemaService {
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final SeatAvailabilityService seatAvailabilityService;
    private final TicketFactoryRegistry ticketFactoryRegistry;
    private final SessionSubscriptionService sessionSubscriptionService;

    public CinemaService(SessionRepository sessionRepository,
                         TicketRepository ticketRepository,
                         SeatAvailabilityService seatAvailabilityService,
                         TicketFactoryRegistry ticketFactoryRegistry,
                         SessionSubscriptionService sessionSubscriptionService) {
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
        this.seatAvailabilityService = seatAvailabilityService;
        this.ticketFactoryRegistry = ticketFactoryRegistry;
        this.sessionSubscriptionService = sessionSubscriptionService;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll().stream()
                .sorted(Comparator.comparing(Session::getStartTime))
                .toList();
    }

    public Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    public List<Seat> getAvailableSeats(UUID sessionId) {
        return seatAvailabilityService.getAvailableSeats(sessionId);
    }

    public Ticket reserveTicket(UUID sessionId, int row, int number, String customerName) {
        return issueTicket(sessionId, row, number, customerName, TicketStatus.RESERVED);
    }

    public Ticket purchaseTicket(UUID sessionId, int row, int number, String customerName) {
        return issueTicket(sessionId, row, number, customerName, TicketStatus.PURCHASED);
    }

    public Ticket purchaseReservedTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        Ticket purchasedTicket = ticket.markPurchased();
        ticketRepository.save(purchasedTicket);
        sessionSubscriptionService.subscribe(ticket.getSessionId(), ticket.getCustomerName());
        return purchasedTicket;
    }

    public Ticket cancelTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        Ticket cancelledTicket = ticket.cancel();
        ticketRepository.save(cancelledTicket);
        return cancelledTicket;
    }

    public List<Ticket> getTicketsForSession(UUID sessionId) {
        getSession(sessionId);
        return ticketRepository.findBySessionId(sessionId);
    }

    private Ticket issueTicket(UUID sessionId, int row, int number, String customerName, TicketStatus status) {
        Session session = getSession(sessionId);
        Seat seat = new Seat(row, number);

        if (!seatAvailabilityService.isSeatAvailable(sessionId, seat)) {
            throw new SeatAlreadyBookedException("Seat is already reserved or purchased: " + seat);
        }

        Ticket ticket = ticketFactoryRegistry.create(status, session, seat, customerName);
        ticketRepository.save(ticket);
        sessionSubscriptionService.subscribe(sessionId, customerName);
        return ticket;
    }
}
