package org.example.service;

import org.example.domain.exception.EntityInUseException;
import org.example.domain.model.Movie;
import org.example.domain.repository.SessionRepository;
import org.example.integration.reference.ReferenceServiceClient;
import org.example.infrastructure.trace.ServiceLogger;
import org.example.infrastructure.trace.TraceContext;

import java.util.List;
import java.util.UUID;

public class RemoteMovieService implements MovieOperations {
    private final ReferenceServiceClient referenceClient;
    private final SessionRepository sessionRepository;
    private final ServiceLogger logger = ServiceLogger.forComponent("core-service", RemoteMovieService.class);

    public RemoteMovieService(ReferenceServiceClient referenceClient, SessionRepository sessionRepository) {
        this.referenceClient = referenceClient;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        TraceContext.ensureTraceId();
        logger.info("Listing movies through reference service");
        return referenceClient.getAllMovies();
    }

    @Override
    public Movie getMovie(UUID id) {
        TraceContext.ensureTraceId();
        logger.info("Getting movie " + id + " through reference service");
        return referenceClient.getMovie(id);
    }

    @Override
    public Movie createMovie(String title, int durationMinutes, String genre, String ageRating) {
        TraceContext.ensureTraceId();
        logger.info("Creating movie through reference service");
        return referenceClient.createMovie(title, durationMinutes, genre, ageRating);
    }

    @Override
    public Movie updateMovie(UUID id, String title, int durationMinutes, String genre, String ageRating) {
        TraceContext.ensureTraceId();
        logger.info("Updating movie " + id + " through reference service");
        return referenceClient.updateMovie(id, title, durationMinutes, genre, ageRating);
    }

    @Override
    public void deleteMovie(UUID id) {
        TraceContext.ensureTraceId();
        boolean inUse = sessionRepository.findAll().stream()
                .anyMatch(session -> session.getMovie().getId().equals(id));
        if (inUse) {
            throw new EntityInUseException("Movie is used in existing sessions and cannot be removed.");
        }
        logger.info("Deleting movie " + id + " through reference service");
        referenceClient.deleteMovie(id);
    }
}
