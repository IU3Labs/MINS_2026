package org.example.service;

import org.example.domain.exception.NotFoundException;
import org.example.domain.exception.ScheduleConflictException;
import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Session;
import org.example.domain.repository.HallRepository;
import org.example.domain.repository.MovieRepository;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.service.subscription.SessionSubscriptionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class SessionService {
    private final SessionRepository sessionRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final TicketRepository ticketRepository;
    private final SessionSubscriptionService sessionSubscriptionService;

    public SessionService(SessionRepository sessionRepository,
                          MovieRepository movieRepository,
                          HallRepository hallRepository,
                          TicketRepository ticketRepository,
                          SessionSubscriptionService sessionSubscriptionService) {
        this.sessionRepository = sessionRepository;
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
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
        Movie movie = getMovie(movieId);
        Hall hall = getHall(hallId);

        Session session = new Session(UUID.randomUUID(), movie, hall, startTime, price);
        validateSchedule(session);
        sessionRepository.save(session);
        return session;
    }

    public Session updateSession(UUID sessionId, UUID movieId, UUID hallId, LocalDateTime startTime, BigDecimal price) {
        Session existingSession = getSession(sessionId);
        Movie movie = getMovie(movieId);
        Hall hall = getHall(hallId);

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

    private Movie getMovie(UUID movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found: " + movieId));
    }

    private Hall getHall(UUID hallId) {
        return hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException("Hall not found: " + hallId));
    }
}
