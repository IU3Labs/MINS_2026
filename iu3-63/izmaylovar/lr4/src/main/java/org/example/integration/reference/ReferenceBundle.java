package org.example.integration.reference;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;

public record ReferenceBundle(Movie movie, Hall hall) {
}
