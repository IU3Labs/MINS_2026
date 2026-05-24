package org.example.service;

import org.example.domain.exception.InvalidSeatException;
import org.example.domain.exception.ValidationException;
import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Session;
import org.example.domain.repository.HallRepository;
import org.example.domain.repository.MovieRepository;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.domain.repository.inmemory.InMemoryHallRepository;
import org.example.domain.repository.inmemory.InMemoryMovieRepository;
import org.example.domain.repository.inmemory.InMemorySessionRepository;
import org.example.domain.repository.inmemory.InMemoryTicketRepository;
import org.example.service.factory.PurchasedTicketFactory;
import org.example.service.factory.ReservedTicketFactory;
import org.example.service.factory.TicketFactoryRegistry;
import org.example.service.subscription.SessionSubscriptionService;
import org.example.service.validation.CustomerNameValidationHandler;
import org.example.service.validation.SeatAvailabilityValidationHandler;
import org.example.service.validation.SeatExistsValidationHandler;
import org.example.service.validation.TicketIssuingValidationHandler;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CinemaServiceValidationTest {
    @Test
    void rejectsBlankCustomerNameBeforeTicketCreation() {
        CinemaService cinemaService = createCinemaService();
        Session session = cinemaService.getAllSessions().getFirst();

        assertThrows(ValidationException.class, () -> cinemaService.reserveTicket(session.getId(), 1, 1, " "));
    }

    @Test
    void rejectsSeatsOutsideHallCapacity() {
        CinemaService cinemaService = createCinemaService();
        Session session = cinemaService.getAllSessions().getFirst();

        assertThrows(InvalidSeatException.class, () -> cinemaService.purchaseTicket(session.getId(), 99, 1, "Alice"));
    }

    private CinemaService createCinemaService() {
        HallRepository hallRepository = new InMemoryHallRepository();
        MovieRepository movieRepository = new InMemoryMovieRepository();
        SessionRepository sessionRepository = new InMemorySessionRepository();
        TicketRepository ticketRepository = new InMemoryTicketRepository();

        Hall hall = new Hall(UUID.randomUUID(), "Main Hall", 5, 5);
        Movie movie = new Movie(UUID.randomUUID(), "Dune", 166, "Sci-Fi", "16+");
        Session session = new Session(
                UUID.randomUUID(),
                movie,
                hall,
                LocalDateTime.of(2026, 4, 8, 18, 0),
                new BigDecimal("500")
        );

        hallRepository.save(hall);
        movieRepository.save(movie);
        sessionRepository.save(session);

        SeatAvailabilityService seatAvailabilityService = new SeatAvailabilityService(sessionRepository, ticketRepository);
        TicketIssuingValidationHandler validationChain = new CustomerNameValidationHandler();
        validationChain.linkWith(new SeatExistsValidationHandler())
                .linkWith(new SeatAvailabilityValidationHandler(seatAvailabilityService));

        return new CinemaService(
                sessionRepository,
                ticketRepository,
                seatAvailabilityService,
                new TicketFactoryRegistry(List.of(new ReservedTicketFactory(), new PurchasedTicketFactory())),
                new SessionSubscriptionService(),
                validationChain
        );
    }
}
