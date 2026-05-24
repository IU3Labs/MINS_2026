package org.example.integration.reference;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.grpc.reference.HallDto;
import org.example.grpc.reference.MovieDto;
import org.example.grpc.reference.ReferenceBundleResponse;

import java.util.UUID;

public final class ReferenceGrpcMapper {
    private ReferenceGrpcMapper() {
    }

    public static Movie toMovie(MovieDto movieDto) {
        return new Movie(
                UUID.fromString(movieDto.getId()),
                movieDto.getTitle(),
                movieDto.getDurationMinutes(),
                movieDto.getGenre(),
                movieDto.getAgeRating()
        );
    }

    public static Hall toHall(HallDto hallDto) {
        return new Hall(
                UUID.fromString(hallDto.getId()),
                hallDto.getName(),
                hallDto.getRows(),
                hallDto.getSeatsPerRow()
        );
    }

    public static ReferenceBundle toReferenceBundle(ReferenceBundleResponse response) {
        return new ReferenceBundle(toMovie(response.getMovie()), toHall(response.getHall()));
    }

    public static MovieDto toMovieDto(Movie movie) {
        return MovieDto.newBuilder()
                .setId(movie.getId().toString())
                .setTitle(movie.getTitle())
                .setDurationMinutes(movie.getDurationMinutes())
                .setGenre(movie.getGenre())
                .setAgeRating(movie.getAgeRating())
                .build();
    }

    public static HallDto toHallDto(Hall hall) {
        return HallDto.newBuilder()
                .setId(hall.getId().toString())
                .setName(hall.getName())
                .setRows(hall.getRows())
                .setSeatsPerRow(hall.getSeatsPerRow())
                .build();
    }
}
