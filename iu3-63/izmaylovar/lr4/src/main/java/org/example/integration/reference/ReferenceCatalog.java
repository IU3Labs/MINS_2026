package org.example.integration.reference;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;

import java.util.UUID;

public interface ReferenceCatalog {
    Movie getMovie(UUID movieId);

    Hall getHall(UUID hallId);

    ReferenceBundle getReferenceBundle(UUID movieId, UUID hallId);
}
