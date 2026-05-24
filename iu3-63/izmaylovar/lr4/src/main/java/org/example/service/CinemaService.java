package org.example.service;

import org.example.domain.exception.SessionNotFoundException;
import org.example.domain.exception.TicketNotFoundException;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.infrastructure.trace.ServiceLogger;
import org.example.infrastructure.trace.TraceContext;
import org.example.service.factory.TicketFactoryRegistry;
import org.example.service.subscription.SessionSubscriptionService;
import org.example.service.validation.TicketIssuingContext;
import org.example.service.validation.TicketIssuingValidationHandler;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CinemaService {
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final SeatAvailabilityService seatAvailabilityService;
    private final TicketFactoryRegistry ticketFactoryRegistry;
    private final SessionSubscriptionService sessionSubscriptionService;
    private final TicketIssuingValidationHandler ticketIssuingValidationHandler;
    private final ServiceLogger logger = ServiceLogger.forComponent("core-service", CinemaService.class);

    public CinemaService(SessionRepository sessionRepository,
                         TicketRepository ticketRepository,
                         SeatAvailabilityService seatAvailabilityService,
                         TicketFactoryRegistry ticketFactoryRegistry,
                         SessionSubscriptionService sessionSubscriptionService,
                         TicketIssuingValidationHandler ticketIssuingValidationHandler) {
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
        this.seatAvailabilityService = seatAvailabilityService;
        this.ticketFactoryRegistry = ticketFactoryRegistry;
        this.sessionSubscriptionService = sessionSubscriptionService;
        this.ticketIssuingValidationHandler = ticketIssuingValidationHandler;
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
        TraceContext.ensureTraceId();
        logger.info("Reserving ticket for session " + sessionId + ", seat " + row + "-" + number);
        return issueTicket(sessionId, row, number, customerName, TicketStatus.RESERVED);
    }

    public Ticket purchaseTicket(UUID sessionId, int row, int number, String customerName) {
        TraceContext.ensureTraceId();
        logger.info("Purchasing ticket for session " + sessionId + ", seat " + row + "-" + number);
        return issueTicket(sessionId, row, number, customerName, TicketStatus.PURCHASED);
    }

    public Ticket purchaseReservedTicket(UUID ticketId) {
        TraceContext.ensureTraceId();
        logger.info("Purchasing reserved ticket " + ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        Ticket purchasedTicket = ticket.markPurchased();
        ticketRepository.save(purchasedTicket);
        sessionSubscriptionService.subscribe(ticket.getSessionId(), ticket.getCustomerName());
        return purchasedTicket;
    }

    public Ticket cancelTicket(UUID ticketId) {
        TraceContext.ensureTraceId();
        logger.info("Cancelling ticket " + ticketId);
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
        ticketIssuingValidationHandler.validate(new TicketIssuingContext(session, seat, customerName));

        Ticket ticket = ticketFactoryRegistry.create(status, session, seat, customerName);
        ticketRepository.save(ticket);
        sessionSubscriptionService.subscribe(sessionId, customerName);
        return ticket;
    }
}
