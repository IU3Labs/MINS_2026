package com.cinema.reference.server;

import com.cinema.proto.reference.ReferenceServiceGrpc;
import com.cinema.proto.reference.ReferenceServiceOuterClass.*;
import com.cinema.reference.entity.*;
import com.cinema.reference.service.*;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    private final MovieService movieService;
    private final HallService hallService;
    private final ScreeningService screeningService;

    public ReferenceServiceImpl(MovieService movieService,
                                HallService hallService,
                                ScreeningService screeningService) {
        this.movieService = movieService;
        this.hallService = hallService;
        this.screeningService = screeningService;
    }

    private String getTraceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "unknown";
    }

    // ==================== MOVIES ====================
    @Override
    public void createMovie(CreateMovieRequest request, StreamObserver<MovieResponse> responseObserver) {
        String traceId = getTraceId();
        String title = request.getTitle();
        int durationMinutes = request.getDurationMinutes();
        String genre = request.getGenre();
        int ageRestriction = request.getAgeRestriction();

        log.info("[{}] createMovie request: title={}, duration={}, genre={}, age={}",
                traceId, title, durationMinutes, genre, ageRestriction);

        // === Валидация входных параметров ===
        if (title == null || title.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Movie title cannot be empty")
                    .asRuntimeException());
            return;
        }

        if (durationMinutes <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Duration must be positive")
                    .asRuntimeException());
            return;
        }

        if (genre == null || genre.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Genre cannot be empty")
                    .asRuntimeException());
            return;
        }

        if (ageRestriction < 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Age restriction cannot be negative")
                    .asRuntimeException());
            return;
        }

        try {
            movieService.create(title, durationMinutes, genre, ageRestriction);

            Movie movie = movieService.getAll().stream()
                    .filter(m -> m.getTitle().equals(title))
                    .findFirst()
                    .orElse(null);

            if (movie == null) {
                responseObserver.onError(Status.INTERNAL
                        .withDescription("Movie created but not found")
                        .asRuntimeException());
                return;
            }

            responseObserver.onNext(toProto(movie));
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.warn("[{}] Invalid argument: {}", traceId, e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getMovie(GetMovieRequest request, StreamObserver<MovieResponse> responseObserver) {
        String traceId = getTraceId();
        String movieId = request.getId();

        log.info("[{}] getMovie request: id={}", traceId, movieId);

        if (movieId == null || movieId.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Movie ID cannot be empty")
                    .asRuntimeException());
            return;
        }

        try {
            UUID id = UUID.fromString(movieId);
            Movie movie = movieService.getById(id).orElse(null);

            if (movie == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Movie not found: " + movieId)
                        .asRuntimeException());
                return;
            }

            responseObserver.onNext(toProto(movie));
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + movieId)
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void listMovies(Empty request, StreamObserver<MovieListResponse> responseObserver) {
        String traceId = getTraceId();
        log.info("[{}] listMovies request", traceId);

        try {
            MovieListResponse.Builder builder = MovieListResponse.newBuilder();
            for (Movie movie : movieService.getAll()) {
                builder.addMovies(toProto(movie));
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    // ==================== HALLS ====================
    @Override
    public void createHall(CreateHallRequest request, StreamObserver<HallResponse> responseObserver) {
        String traceId = getTraceId();
        String name = request.getName();
        int rows = request.getRows();
        int seatsPerRow = request.getSeatsPerRow();

        log.info("[{}] createHall request: name={}, rows={}, seatsPerRow={}",
                traceId, name, rows, seatsPerRow);

        // === Валидация входных параметров ===
        if (name == null || name.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Hall name cannot be empty")
                    .asRuntimeException());
            return;
        }

        if (rows <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Number of rows must be positive")
                    .asRuntimeException());
            return;
        }

        if (seatsPerRow <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Number of seats per row must be positive")
                    .asRuntimeException());
            return;
        }

        try {
            hallService.create(name, rows, seatsPerRow);

            Hall hall = hallService.getAll().stream()
                    .filter(h -> h.getName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (hall == null) {
                responseObserver.onError(Status.INTERNAL
                        .withDescription("Hall created but not found")
                        .asRuntimeException());
                return;
            }

            responseObserver.onNext(toProto(hall));
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.warn("[{}] Invalid argument: {}", traceId, e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getHall(GetHallRequest request, StreamObserver<HallResponse> responseObserver) {
        String traceId = getTraceId();
        String hallId = request.getId();

        log.info("[{}] getHall request: id={}", traceId, hallId);

        if (hallId == null || hallId.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Hall ID cannot be empty")
                    .asRuntimeException());
            return;
        }

        try {
            UUID id = UUID.fromString(hallId);
            Hall hall = hallService.getById(id).orElse(null);

            if (hall == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Hall not found: " + hallId)
                        .asRuntimeException());
                return;
            }

            responseObserver.onNext(toProto(hall));
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + hallId)
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void listHalls(Empty request, StreamObserver<HallListResponse> responseObserver) {
        String traceId = getTraceId();
        log.info("[{}] listHalls request", traceId);

        try {
            HallListResponse.Builder builder = HallListResponse.newBuilder();
            for (Hall hall : hallService.getAll()) {
                builder.addHalls(toProto(hall));
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    // ==================== SCREENINGS ====================
    @Override
    public void createScreening(CreateScreeningRequest request, StreamObserver<ScreeningResponse> responseObserver) {
        String traceId = getTraceId();
        String movieId = request.getMovieId();
        String hallId = request.getHallId();
        String startTimeStr = request.getStartTime();
        double ticketPrice = request.getTicketPrice();

        log.info("[{}] createScreening request: movieId={}, hallId={}, startTime={}, price={}",
                traceId, movieId, hallId, startTimeStr, ticketPrice);

        // === Валидация: проверяем, что ID не пустые ===
        if (movieId == null || movieId.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Movie ID cannot be empty")
                    .asRuntimeException());
            return;
        }

        if (hallId == null || hallId.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Hall ID cannot be empty")
                    .asRuntimeException());
            return;
        }

        // === Валидация: цена билета ===
        if (ticketPrice <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Ticket price must be positive")
                    .asRuntimeException());
            return;
        }

        // === Валидация: формат даты ===
        LocalDateTime startTime;
        try {
            startTime = LocalDateTime.parse(startTimeStr);
        } catch (DateTimeParseException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid date format. Expected: yyyy-MM-ddTHH:mm:ss")
                    .asRuntimeException());
            return;
        }

        // === Валидация: сеанс не может быть в прошлом ===
        if (startTime.isBefore(LocalDateTime.now())) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Cannot create screening in the past")
                    .asRuntimeException());
            return;
        }

        // === Валидация: проверяем, что фильм существует ===
        UUID movieUuid;
        try {
            movieUuid = UUID.fromString(movieId);
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid movie ID format: " + movieId)
                    .asRuntimeException());
            return;
        }

        if (movieService.getById(movieUuid).isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Movie not found: " + movieId)
                    .asRuntimeException());
            return;
        }

        // === Валидация: проверяем, что зал существует ===
        UUID hallUuid;
        try {
            hallUuid = UUID.fromString(hallId);
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid hall ID format: " + hallId)
                    .asRuntimeException());
            return;
        }

        if (hallService.getById(hallUuid).isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Hall not found: " + hallId)
                    .asRuntimeException());
            return;
        }

        try {
            Result<UUID> result = screeningService.create(movieUuid, hallUuid, startTime, ticketPrice);

            if (result.isSuccess()) {
                responseObserver.onNext(ScreeningResponse.newBuilder()
                        .setId(result.getValue().toString())
                        .build());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription(result.getError())
                        .asRuntimeException());
            }

        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getScreening(GetScreeningRequest request, StreamObserver<ScreeningDetailResponse> responseObserver) {
        String traceId = getTraceId();
        String screeningId = request.getScreeningId();

        log.info("[{}] getScreening request: id={}", traceId, screeningId);

        if (screeningId == null || screeningId.trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Screening ID cannot be empty")
                    .asRuntimeException());
            return;
        }

        try {
            UUID id = UUID.fromString(screeningId);
            Screening screening = screeningService.getById(id).orElse(null);

            if (screening == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Screening not found: " + screeningId)
                        .asRuntimeException());
                return;
            }

            responseObserver.onNext(ScreeningDetailResponse.newBuilder()
                    .setId(screening.getId().toString())
                    .setMovieId(screening.getMovie().getId().toString())
                    .setHallId(screening.getHall().getId().toString())
                    .setStartTime(screening.getStartTime().toString())
                    .setTicketPrice(screening.getTicketPrice())
                    .setRows(screening.getHall().getRows())
                    .setSeatsPerRow(screening.getHall().getSeatsPerRow())
                    .build());
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + screeningId)
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void listScreenings(Empty request, StreamObserver<ScreeningListResponse> responseObserver) {
        String traceId = getTraceId();
        log.info("[{}] listScreenings request", traceId);

        try {
            ScreeningListResponse.Builder builder = ScreeningListResponse.newBuilder();
            for (Screening screening : screeningService.getAll()) {
                builder.addScreenings(ScreeningDetailResponse.newBuilder()
                        .setId(screening.getId().toString())
                        .setMovieId(screening.getMovie().getId().toString())
                        .setHallId(screening.getHall().getId().toString())
                        .setStartTime(screening.getStartTime().toString())
                        .setTicketPrice(screening.getTicketPrice())
                        .setRows(screening.getHall().getRows())
                        .setSeatsPerRow(screening.getHall().getSeatsPerRow())
                        .build());
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[{}] Unexpected error", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    // ==================== CONVERTERS ====================
    private MovieResponse toProto(Movie movie) {
        return MovieResponse.newBuilder()
                .setId(movie.getId().toString())
                .setTitle(movie.getTitle())
                .setDurationMinutes((int) movie.getDuration().toMinutes())
                .setGenre(movie.getGenre())
                .setAgeRestriction(movie.getAgeRestriction())
                .build();
    }

    private HallResponse toProto(Hall hall) {
        return HallResponse.newBuilder()
                .setId(hall.getId().toString())
                .setName(hall.getName())
                .setRows(hall.getRows())
                .setSeatsPerRow(hall.getSeatsPerRow())
                .build();
    }
}