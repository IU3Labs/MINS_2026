package org.example.service;

import org.example.domain.exception.NotFoundException;
import org.example.domain.exception.ScheduleConflictException;
import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Session;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.infrastructure.trace.ServiceLogger;
import org.example.infrastructure.trace.TraceContext;
import org.example.integration.reference.ReferenceCatalog;
import org.example.service.subscription.SessionSubscriptionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class SessionService {
    private final SessionRepository sessionRepository;
    private final ReferenceCatalog referenceCatalog;
    private final TicketRepository ticketRepository;
    private final SessionSubscriptionService sessionSubscriptionService;
    private final ServiceLogger logger = ServiceLogger.forComponent("core-service", SessionService.class);

    public SessionService(SessionRepository sessionRepository,
                          ReferenceCatalog referenceCatalog,
                          TicketRepository ticketRepository,
                          SessionSubscriptionService sessionSubscriptionService) {
        this.sessionRepository = sessionRepository;
        this.referenceCatalog = referenceCatalog;
        this.ticketRepository = ticketRepository;
        this.sessionSubscriptionService = sessionSubscriptionService;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll().stream()
                .sorted(Comparator.comparing(Session::getStartTime))
                .toList();
    }

    public Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found: " + sessionId));
    }

    public Session createSession(UUID movieId, UUID hallId, LocalDateTime startTime, BigDecimal price) {
        TraceContext.ensureTraceId();
        logger.info("Creating session for movie " + movieId + " and hall " + hallId);
        var referenceBundle = referenceCatalog.getReferenceBundle(movieId, hallId);
        Movie movie = referenceBundle.movie();
        Hall hall = referenceBundle.hall();

        Session session = new Session(UUID.randomUUID(), movie, hall, startTime, price);
        validateSchedule(session);
        sessionRepository.save(session);
        return session;
    }

    public Session updateSession(UUID sessionId, UUID movieId, UUID hallId, LocalDateTime startTime, BigDecimal price) {
        TraceContext.ensureTraceId();
        logger.info("Updating session " + sessionId);
        Session existingSession = getSession(sessionId);
        var referenceBundle = referenceCatalog.getReferenceBundle(movieId, hallId);
        Movie movie = referenceBundle.movie();
        Hall hall = referenceBundle.hall();

        Session updatedSession = existingSession.update(movie, hall, startTime, price);
        validateSchedule(updatedSession);
        sessionRepository.save(updatedSession);

        sessionSubscriptionService.notifySessionUpdated(
                updatedSession,
                "Session details were changed. Check the updated start time, hall and price."
        );

        return updatedSession;
    }

    public void deleteSession(UUID sessionId) {
        TraceContext.ensureTraceId();
        logger.info("Deleting session " + sessionId);
        Session session = getSession(sessionId);
        ticketRepository.deleteBySessionId(sessionId);
        sessionRepository.delete(session);
        sessionSubscriptionService.notifySessionCancelled(session);
    }

    private void validateSchedule(Session candidate) {
        boolean hasConflict = sessionRepository.findAll().stream()
                .filter(existing -> !existing.getId().equals(candidate.getId()))
                .anyMatch(candidate::overlapsWith);

        if (hasConflict) {
            throw new ScheduleConflictException("Hall already has another session at this time.");
        }
    }
}
