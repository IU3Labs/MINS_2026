package org.example.integration.reference;

import org.example.domain.exception.NotFoundException;
import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.repository.HallRepository;
import org.example.domain.repository.MovieRepository;

import java.util.UUID;

public class LocalReferenceCatalog implements ReferenceCatalog {
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    public LocalReferenceCatalog(MovieRepository movieRepository, HallRepository hallRepository) {
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public Movie getMovie(UUID movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found: " + movieId));
    }

    @Override
    public Hall getHall(UUID hallId) {
        return hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException("Hall not found: " + hallId));
    }

    @Override
    public ReferenceBundle getReferenceBundle(UUID movieId, UUID hallId) {
        return new ReferenceBundle(getMovie(movieId), getHall(hallId));
    }
}
