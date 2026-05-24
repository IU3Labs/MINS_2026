package org.example.integration.reference;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.ClientInterceptors;
import org.example.domain.exception.CinemaException;
import org.example.domain.exception.EntityInUseException;
import org.example.domain.exception.NotFoundException;
import org.example.domain.exception.ValidationException;
import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.grpc.reference.CreateHallRequest;
import org.example.grpc.reference.CreateMovieRequest;
import org.example.grpc.reference.HallDto;
import org.example.grpc.reference.HallListResponse;
import org.example.grpc.reference.MovieDto;
import org.example.grpc.reference.MovieListResponse;
import org.example.grpc.reference.ReferenceBundleRequest;
import org.example.grpc.reference.ReferenceServiceGrpc;
import org.example.grpc.reference.UuidRequest;
import org.example.grpc.reference.UpdateHallRequest;
import org.example.grpc.reference.UpdateMovieRequest;
import org.example.infrastructure.grpc.TraceIdClientInterceptor;
import org.example.infrastructure.trace.ServiceLogger;
import org.example.infrastructure.trace.TraceContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReferenceServiceClient implements ReferenceCatalog, AutoCloseable {
    private static final String UNAVAILABLE_MESSAGE = "Reference service is unavailable. Try again later.";
    private static final long RPC_DEADLINE_SECONDS = 3;

    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub baseBlockingStub;
    private final ServiceLogger logger = ServiceLogger.forComponent("core-service", ReferenceServiceClient.class);

    public ReferenceServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.baseBlockingStub = ReferenceServiceGrpc.newBlockingStub(
                ClientInterceptors.intercept(channel, new TraceIdClientInterceptor())
        );
    }

    public List<Movie> getAllMovies() {
        TraceContext.ensureTraceId();
        logger.info("Requesting movie catalog from reference service");
        try {
            MovieListResponse response = blockingStub().listMovies(Empty.getDefaultInstance());
            return response.getMoviesList().stream()
                    .map(ReferenceGrpcMapper::toMovie)
                    .toList();
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    @Override
    public Movie getMovie(UUID movieId) {
        TraceContext.ensureTraceId();
        logger.info("Requesting movie " + movieId + " from reference service");
        try {
            MovieDto response = blockingStub().getMovie(uuidRequest(movieId));
            return ReferenceGrpcMapper.toMovie(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public Movie createMovie(String title, int durationMinutes, String genre, String ageRating) {
        TraceContext.ensureTraceId();
        logger.info("Creating movie through reference service");
        try {
            MovieDto response = blockingStub().createMovie(
                    CreateMovieRequest.newBuilder()
                            .setTitle(title)
                            .setDurationMinutes(durationMinutes)
                            .setGenre(genre)
                            .setAgeRating(ageRating)
                            .build()
            );
            return ReferenceGrpcMapper.toMovie(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public Movie updateMovie(UUID movieId, String title, int durationMinutes, String genre, String ageRating) {
        TraceContext.ensureTraceId();
        logger.info("Updating movie " + movieId + " through reference service");
        try {
            MovieDto response = blockingStub().updateMovie(
                    UpdateMovieRequest.newBuilder()
                            .setId(movieId.toString())
                            .setTitle(title)
                            .setDurationMinutes(durationMinutes)
                            .setGenre(genre)
                            .setAgeRating(ageRating)
                            .build()
            );
            return ReferenceGrpcMapper.toMovie(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public void deleteMovie(UUID movieId) {
        TraceContext.ensureTraceId();
        logger.info("Deleting movie " + movieId + " through reference service");
        try {
            blockingStub().deleteMovie(uuidRequest(movieId));
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public List<Hall> getAllHalls() {
        TraceContext.ensureTraceId();
        logger.info("Requesting hall catalog from reference service");
        try {
            HallListResponse response = blockingStub().listHalls(Empty.getDefaultInstance());
            return response.getHallsList().stream()
                    .map(ReferenceGrpcMapper::toHall)
                    .toList();
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    @Override
    public Hall getHall(UUID hallId) {
        TraceContext.ensureTraceId();
        logger.info("Requesting hall " + hallId + " from reference service");
        try {
            HallDto response = blockingStub().getHall(uuidRequest(hallId));
            return ReferenceGrpcMapper.toHall(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public Hall createHall(String name, int rows, int seatsPerRow) {
        TraceContext.ensureTraceId();
        logger.info("Creating hall through reference service");
        try {
            HallDto response = blockingStub().createHall(
                    CreateHallRequest.newBuilder()
                            .setName(name)
                            .setRows(rows)
                            .setSeatsPerRow(seatsPerRow)
                            .build()
            );
            return ReferenceGrpcMapper.toHall(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public Hall updateHall(UUID hallId, String name, int rows, int seatsPerRow) {
        TraceContext.ensureTraceId();
        logger.info("Updating hall " + hallId + " through reference service");
        try {
            HallDto response = blockingStub().updateHall(
                    UpdateHallRequest.newBuilder()
                            .setId(hallId.toString())
                            .setName(name)
                            .setRows(rows)
                            .setSeatsPerRow(seatsPerRow)
                            .build()
            );
            return ReferenceGrpcMapper.toHall(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    public void deleteHall(UUID hallId) {
        TraceContext.ensureTraceId();
        logger.info("Deleting hall " + hallId + " through reference service");
        try {
            blockingStub().deleteHall(uuidRequest(hallId));
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    @Override
    public ReferenceBundle getReferenceBundle(UUID movieId, UUID hallId) {
        TraceContext.ensureTraceId();
        logger.info("Requesting movie/hall bundle for session creation");
        try {
            var response = blockingStub().getReferenceBundle(
                    ReferenceBundleRequest.newBuilder()
                            .setMovieId(movieId.toString())
                            .setHallId(hallId.toString())
                            .build()
            );
            return ReferenceGrpcMapper.toReferenceBundle(response);
        } catch (StatusRuntimeException e) {
            throw translateException(e);
        }
    }

    @Override
    public void close() {
        channel.shutdown();
        try {
            if (!channel.awaitTermination(3, TimeUnit.SECONDS)) {
                channel.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            channel.shutdownNow();
        }
    }

    private UuidRequest uuidRequest(UUID id) {
        return UuidRequest.newBuilder()
                .setId(id.toString())
                .build();
    }

    private ReferenceServiceGrpc.ReferenceServiceBlockingStub blockingStub() {
        channel.resetConnectBackoff();
        return baseBlockingStub
                .withWaitForReady()
                .withDeadlineAfter(RPC_DEADLINE_SECONDS, TimeUnit.SECONDS);
    }

    private CinemaException translateException(StatusRuntimeException exception) {
        Status status = exception.getStatus();
        String description = status.getDescription();
        logger.error("Reference service gRPC call failed with status " + formatStatus(status));
        return switch (status.getCode()) {
            case UNAVAILABLE, DEADLINE_EXCEEDED -> new ReferenceServiceUnavailableException(UNAVAILABLE_MESSAGE);
            case NOT_FOUND -> new NotFoundException(description == null ? "Reference entity not found." : description);
            case INVALID_ARGUMENT -> new ValidationException(description == null ? "Reference validation failed." : description);
            case FAILED_PRECONDITION -> new EntityInUseException(description == null ? "Reference entity is in use." : description);
            default -> new CinemaException(description == null ? "Reference service request failed." : description);
        };
    }

    private String formatStatus(Status status) {
        String description = status.getDescription();
        if (description == null || description.isBlank()) {
            return status.getCode().toString();
        }

        String firstLine = description.lines().findFirst().orElse(description);
        if (firstLine.length() > 240) {
            firstLine = firstLine.substring(0, 240) + "...";
        }
        return status.getCode() + ": " + firstLine;
    }
}
