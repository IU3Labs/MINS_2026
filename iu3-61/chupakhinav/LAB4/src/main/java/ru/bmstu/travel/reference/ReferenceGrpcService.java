package ru.bmstu.travel.reference;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import ru.bmstu.travel.common.Tracing;
import ru.bmstu.travel.proto.reference.AddClientRequest;
import ru.bmstu.travel.proto.reference.AddTourRequest;
import ru.bmstu.travel.proto.reference.ClientMutationResponse;
import ru.bmstu.travel.proto.reference.ClientRequest;
import ru.bmstu.travel.proto.reference.ClientResponse;
import ru.bmstu.travel.proto.reference.Empty;
import ru.bmstu.travel.proto.reference.ListClientsResponse;
import ru.bmstu.travel.proto.reference.ListToursResponse;
import ru.bmstu.travel.proto.reference.ReferenceServiceGrpc;
import ru.bmstu.travel.proto.reference.TourMutationResponse;
import ru.bmstu.travel.proto.reference.TourRequest;
import ru.bmstu.travel.proto.reference.TourResponse;
import ru.bmstu.travel.reference.model.Client;
import ru.bmstu.travel.reference.model.Discount;
import ru.bmstu.travel.reference.model.ReferenceDiscountType;
import ru.bmstu.travel.reference.model.Tour;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ReferenceGrpcService extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger logger = Logger.getLogger(ReferenceGrpcService.class.getName());
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^C-\\d{3}$");
    private static final Pattern TOUR_ID_PATTERN = Pattern.compile("^T-\\d{3}$");

    private final ReferenceRepository repository;

    public ReferenceGrpcService() {
        this(new ReferenceRepository());
    }

    public ReferenceGrpcService(ReferenceRepository repository) {
        this.repository = repository == null ? new ReferenceRepository() : repository;
    }

    @Override
    public void getClient(ClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " GetClient client_id=" + request.getClientId());
        try {
            validateId(request.getClientId(), CLIENT_ID_PATTERN, "клиента", "C-001");
            Client client = repository.getClient(request.getClientId());
            if (client == null) {
                throw Status.NOT_FOUND.withDescription("Клиент не найден.").asRuntimeException();
            }
            responseObserver.onNext(ClientResponse.newBuilder().setFound(true).setMessage("OK").setClient(toProtoClient(client)).build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc) {
            responseObserver.onError(exc);
        }
    }

    @Override
    public void getTour(TourRequest request, StreamObserver<TourResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " GetTour tour_id=" + request.getTourId());
        try {
            validateId(request.getTourId(), TOUR_ID_PATTERN, "тура", "T-001");
            Tour tour = repository.getTour(request.getTourId());
            if (tour == null) {
                throw Status.NOT_FOUND.withDescription("Тур не найден.").asRuntimeException();
            }
            responseObserver.onNext(TourResponse.newBuilder().setFound(true).setMessage("OK").setTour(toProtoTour(tour)).build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc) {
            responseObserver.onError(exc);
        }
    }

    @Override
    public void listClients(Empty request, StreamObserver<ListClientsResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ListClients");
        var clients = repository.listClients().stream().map(this::toProtoClient).toList();
        responseObserver.onNext(ListClientsResponse.newBuilder().addAllClients(clients).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listTours(Empty request, StreamObserver<ListToursResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ListTours");
        var tours = repository.listTours().stream().map(this::toProtoTour).toList();
        responseObserver.onNext(ListToursResponse.newBuilder().addAllTours(tours).build());
        responseObserver.onCompleted();
    }

    @Override
    public void addClient(AddClientRequest request, StreamObserver<ClientMutationResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " AddClient full_name=" + request.getFullName());
        try {
            if (request.getFullName().trim().isEmpty() || request.getPhone().trim().isEmpty()) {
                throw Status.INVALID_ARGUMENT.withDescription("ФИО и телефон обязательны.").asRuntimeException();
            }

            Discount discount = null;
            if (request.getHasDiscount()) {
                if (request.getDiscount().getName().trim().isEmpty()) {
                    throw Status.INVALID_ARGUMENT.withDescription("Название скидки обязательно.").asRuntimeException();
                }
                if (request.getDiscount().getPercent() < 0 || request.getDiscount().getPercent() > 100) {
                    throw Status.INVALID_ARGUMENT.withDescription("Процент скидки должен быть в диапазоне 0..100.").asRuntimeException();
                }
                if (request.getDiscount().getType() == ru.bmstu.travel.proto.reference.DiscountType.DISCOUNT_TYPE_UNSPECIFIED) {
                    throw Status.INVALID_ARGUMENT.withDescription("Тип скидки обязателен.").asRuntimeException();
                }
                discount = new Discount(
                        request.getDiscount().getName().trim(),
                        request.getDiscount().getPercent(),
                        ReferenceDiscountType.valueOf(request.getDiscount().getType().name())
                );
            }

            Client client = repository.addClient(request.getFullName(), request.getPhone(), discount);
            responseObserver.onNext(ClientMutationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Клиент добавлен.")
                    .setClient(toProtoClient(client))
                    .build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc) {
            responseObserver.onError(exc);
        }
    }

    @Override
    public void addTour(AddTourRequest request, StreamObserver<TourMutationResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " AddTour title=" + request.getTitle());
        try {
            if (request.getTitle().trim().isEmpty() || request.getDestination().trim().isEmpty()) {
                throw Status.INVALID_ARGUMENT.withDescription("Название тура и направление обязательны.").asRuntimeException();
            }
            if (request.getBasePrice() <= 0) {
                throw Status.INVALID_ARGUMENT.withDescription("Базовая цена должна быть больше 0.").asRuntimeException();
            }
            if (request.getCapacity() <= 0) {
                throw Status.INVALID_ARGUMENT.withDescription("Количество мест должно быть больше 0.").asRuntimeException();
            }

            Tour tour = repository.addTour(request.getTitle(), request.getDestination(), request.getBasePrice(), request.getCapacity());
            responseObserver.onNext(TourMutationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Тур добавлен.")
                    .setTour(toProtoTour(tour))
                    .build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc) {
            responseObserver.onError(exc);
        }
    }

    @Override
    public void reserveTourSeat(TourRequest request, StreamObserver<TourMutationResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ReserveTourSeat tour_id=" + request.getTourId());
        try {
            validateId(request.getTourId(), TOUR_ID_PATTERN, "тура", "T-001");
            Tour tour = repository.getTour(request.getTourId());
            if (tour == null) {
                throw Status.NOT_FOUND.withDescription("Тур не найден.").asRuntimeException();
            }
            if (tour.availableSeats() <= 0) {
                throw Status.RESOURCE_EXHAUSTED.withDescription("В туре не осталось свободных мест.").asRuntimeException();
            }
            Tour updated = repository.reserveTourSeat(request.getTourId());
            responseObserver.onNext(TourMutationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Место успешно зарезервировано.")
                    .setTour(toProtoTour(updated))
                    .build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc) {
            responseObserver.onError(exc);
        }
    }

    @Override
    public void releaseTourSeat(TourRequest request, StreamObserver<TourMutationResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ReleaseTourSeat tour_id=" + request.getTourId());
        try {
            validateId(request.getTourId(), TOUR_ID_PATTERN, "тура", "T-001");
            Tour tour = repository.releaseTourSeat(request.getTourId());
            if (tour == null) {
                throw Status.NOT_FOUND.withDescription("Тур не найден.").asRuntimeException();
            }
            responseObserver.onNext(TourMutationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Место успешно освобождено.")
                    .setTour(toProtoTour(tour))
                    .build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc) {
            responseObserver.onError(exc);
        }
    }

    private void validateId(String value, Pattern pattern, String entityName, String example) {
        if (!pattern.matcher(value).matches()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(String.format("Некорректный ID %s. Ожидаемый формат: %s.", entityName, example))
                    .asRuntimeException();
        }
    }

    private ru.bmstu.travel.proto.reference.Discount toProtoDiscount(Discount discount) {
        if (discount == null) {
            return ru.bmstu.travel.proto.reference.Discount.newBuilder().build();
        }
        return ru.bmstu.travel.proto.reference.Discount.newBuilder()
                .setName(discount.name())
                .setPercent(discount.percent())
                .setType(ru.bmstu.travel.proto.reference.DiscountType.valueOf(discount.discountType().name()))
                .build();
    }

    private ru.bmstu.travel.proto.reference.Client toProtoClient(Client client) {
        return ru.bmstu.travel.proto.reference.Client.newBuilder()
                .setId(client.id())
                .setFullName(client.fullName())
                .setPhone(client.phone())
                .setHasDiscount(client.discount() != null)
                .setDiscount(toProtoDiscount(client.discount()))
                .build();
    }

    private ru.bmstu.travel.proto.reference.Tour toProtoTour(Tour tour) {
        return ru.bmstu.travel.proto.reference.Tour.newBuilder()
                .setId(tour.id())
                .setTitle(tour.title())
                .setDestination(tour.destination())
                .setBasePrice(tour.basePrice())
                .setCapacity(tour.capacity())
                .setBookedSeats(tour.bookedSeats())
                .build();
    }
}
