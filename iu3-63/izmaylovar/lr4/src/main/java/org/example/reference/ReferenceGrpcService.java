package org.example.reference;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.domain.exception.CinemaException;
import org.example.domain.exception.EntityInUseException;
import org.example.domain.exception.NotFoundException;
import org.example.domain.exception.ValidationException;
import org.example.grpc.reference.CreateHallRequest;
import org.example.grpc.reference.CreateMovieRequest;
import org.example.grpc.reference.HallListResponse;
import org.example.grpc.reference.MovieListResponse;
import org.example.grpc.reference.ReferenceBundleRequest;
import org.example.grpc.reference.ReferenceBundleResponse;
import org.example.grpc.reference.ReferenceServiceGrpc;
import org.example.grpc.reference.UuidRequest;
import org.example.grpc.reference.UpdateHallRequest;
import org.example.grpc.reference.UpdateMovieRequest;
import org.example.integration.reference.ReferenceGrpcMapper;
import org.example.service.HallService;
import org.example.service.MovieService;
import org.example.infrastructure.trace.ServiceLogger;

import java.util.UUID;

public class ReferenceGrpcService extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private final MovieService movieService;
    private final HallService hallService;
    private final ServiceLogger logger = ServiceLogger.forComponent("reference-service", ReferenceGrpcService.class);

    public ReferenceGrpcService(MovieService movieService, HallService hallService) {
        this.movieService = movieService;
        this.hallService = hallService;
    }

    @Override
    public void listMovies(Empty request, StreamObserver<MovieListResponse> responseObserver) {
        execute(responseObserver, "ListMovies", () -> MovieListResponse.newBuilder()
                .addAllMovies(movieService.getAllMovies().stream()
                        .map(ReferenceGrpcMapper::toMovieDto)
                        .toList())
                .build());
    }

    @Override
    public void getMovie(UuidRequest request, StreamObserver<org.example.grpc.reference.MovieDto> responseObserver) {
        execute(responseObserver, "GetMovie", () -> ReferenceGrpcMapper.toMovieDto(
                movieService.getMovie(UUID.fromString(request.getId()))
        ));
    }

    @Override
    public void createMovie(CreateMovieRequest request, StreamObserver<org.example.grpc.reference.MovieDto> responseObserver) {
        execute(responseObserver, "CreateMovie", () -> ReferenceGrpcMapper.toMovieDto(
                movieService.createMovie(
                        request.getTitle(),
                        request.getDurationMinutes(),
                        request.getGenre(),
                        request.getAgeRating()
                )
        ));
    }

    @Override
    public void updateMovie(UpdateMovieRequest request, StreamObserver<org.example.grpc.reference.MovieDto> responseObserver) {
        execute(responseObserver, "UpdateMovie", () -> ReferenceGrpcMapper.toMovieDto(
                movieService.updateMovie(
                        UUID.fromString(request.getId()),
                        request.getTitle(),
                        request.getDurationMinutes(),
                        request.getGenre(),
                        request.getAgeRating()
                )
        ));
    }

    @Override
    public void deleteMovie(UuidRequest request, StreamObserver<Empty> responseObserver) {
        execute(responseObserver, "DeleteMovie", () -> {
            movieService.deleteMovie(UUID.fromString(request.getId()));
            return Empty.getDefaultInstance();
        });
    }

    @Override
    public void listHalls(Empty request, StreamObserver<HallListResponse> responseObserver) {
        execute(responseObserver, "ListHalls", () -> HallListResponse.newBuilder()
                .addAllHalls(hallService.getAllHalls().stream()
                        .map(ReferenceGrpcMapper::toHallDto)
                        .toList())
                .build());
    }

    @Override
    public void getHall(UuidRequest request, StreamObserver<org.example.grpc.reference.HallDto> responseObserver) {
        execute(responseObserver, "GetHall", () -> ReferenceGrpcMapper.toHallDto(
                hallService.getHall(UUID.fromString(request.getId()))
        ));
    }

    @Override
    public void createHall(CreateHallRequest request, StreamObserver<org.example.grpc.reference.HallDto> responseObserver) {
        execute(responseObserver, "CreateHall", () -> ReferenceGrpcMapper.toHallDto(
                hallService.createHall(
                        request.getName(),
                        request.getRows(),
                        request.getSeatsPerRow()
                )
        ));
    }

    @Override
    public void updateHall(UpdateHallRequest request, StreamObserver<org.example.grpc.reference.HallDto> responseObserver) {
        execute(responseObserver, "UpdateHall", () -> ReferenceGrpcMapper.toHallDto(
                hallService.updateHall(
                        UUID.fromString(request.getId()),
                        request.getName(),
                        request.getRows(),
                        request.getSeatsPerRow()
                )
        ));
    }

    @Override
    public void deleteHall(UuidRequest request, StreamObserver<Empty> responseObserver) {
        execute(responseObserver, "DeleteHall", () -> {
            hallService.deleteHall(UUID.fromString(request.getId()));
            return Empty.getDefaultInstance();
        });
    }

    @Override
    public void getReferenceBundle(ReferenceBundleRequest request,
                                   StreamObserver<ReferenceBundleResponse> responseObserver) {
        execute(responseObserver, "GetReferenceBundle", () -> ReferenceBundleResponse.newBuilder()
                .setMovie(ReferenceGrpcMapper.toMovieDto(movieService.getMovie(UUID.fromString(request.getMovieId()))))
                .setHall(ReferenceGrpcMapper.toHallDto(hallService.getHall(UUID.fromString(request.getHallId()))))
                .build());
    }

    private <T> void execute(StreamObserver<T> responseObserver, String operation, ResponseSupplier<T> supplier) {
        logger.info("Handling gRPC call: " + operation);
        try {
            T response = supplier.get();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            Status status = Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format.")
                    .withCause(e);
            logger.error("gRPC call " + operation + " failed with status " + status.getCode() + ": " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (CinemaException e) {
            Status status = mapCinemaException(e).withCause(e);
            logger.error("gRPC call " + operation + " failed with status " + status.getCode() + ": " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            Status status = Status.INTERNAL
                    .withDescription("Unexpected reference service error.")
                    .withCause(e);
            logger.error("gRPC call " + operation + " failed with status " + status.getCode() + ": " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }

    private Status mapCinemaException(CinemaException exception) {
        if (exception instanceof NotFoundException) {
            return Status.NOT_FOUND.withDescription(exception.getMessage());
        }
        if (exception instanceof ValidationException) {
            return Status.INVALID_ARGUMENT.withDescription(exception.getMessage());
        }
        if (exception instanceof EntityInUseException) {
            return Status.FAILED_PRECONDITION.withDescription(exception.getMessage());
        }
        return Status.UNKNOWN.withDescription(exception.getMessage());
    }

    @FunctionalInterface
    private interface ResponseSupplier<T> {
        T get();
    }
}
