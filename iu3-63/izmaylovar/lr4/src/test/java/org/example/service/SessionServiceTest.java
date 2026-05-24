package org.example.service;

import org.example.domain.exception.ScheduleConflictException;
import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.repository.HallRepository;
import org.example.domain.repository.MovieRepository;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.domain.repository.inmemory.InMemoryHallRepository;
import org.example.domain.repository.inmemory.InMemoryMovieRepository;
import org.example.domain.repository.inmemory.InMemorySessionRepository;
import org.example.domain.repository.inmemory.InMemoryTicketRepository;
import org.example.integration.reference.LocalReferenceCatalog;
import org.example.service.subscription.SessionSubscriptionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionServiceTest {
    @Test
    void rejectsOverlappingSessionsInTheSameHall() {
        HallRepository hallRepository = new InMemoryHallRepository();
        MovieRepository movieRepository = new InMemoryMovieRepository();
        SessionRepository sessionRepository = new InMemorySessionRepository();
        TicketRepository ticketRepository = new InMemoryTicketRepository();

        Hall hall = new Hall(UUID.randomUUID(), "Main Hall", 6, 8);
        Movie firstMovie = new Movie(UUID.randomUUID(), "Dune", 166, "Sci-Fi", "16+");
        Movie secondMovie = new Movie(UUID.randomUUID(), "Interstellar", 169, "Sci-Fi", "12+");

        hallRepository.save(hall);
        movieRepository.save(firstMovie);
        movieRepository.save(secondMovie);

        SessionService sessionService = new SessionService(
                sessionRepository,
                new LocalReferenceCatalog(movieRepository, hallRepository),
                ticketRepository,
                new SessionSubscriptionService()
        );

        sessionService.createSession(
                firstMovie.getId(),
                hall.getId(),
                LocalDateTime.of(2026, 4, 8, 18, 0),
                new BigDecimal("500")
        );

        assertThrows(
                ScheduleConflictException.class,
                () -> sessionService.createSession(
                        secondMovie.getId(),
                        hall.getId(),
                        LocalDateTime.of(2026, 4, 8, 19, 0),
                        new BigDecimal("600")
                )
        );
    }
}
