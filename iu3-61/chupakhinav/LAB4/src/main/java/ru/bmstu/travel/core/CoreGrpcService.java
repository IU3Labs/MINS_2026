package ru.bmstu.travel.core;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import ru.bmstu.travel.common.Tracing;
import ru.bmstu.travel.core.catalog.CatalogClient;
import ru.bmstu.travel.core.catalog.CatalogDiscount;
import ru.bmstu.travel.core.catalog.CatalogTour;
import ru.bmstu.travel.core.exception.BookingStateException;
import ru.bmstu.travel.core.exception.InvalidDataException;
import ru.bmstu.travel.core.exception.NotFoundException;
import ru.bmstu.travel.core.exception.PricingException;
import ru.bmstu.travel.core.exception.TourAvailabilityException;
import ru.bmstu.travel.core.exception.TravelAgencyException;
import ru.bmstu.travel.core.gateway.GrpcReferenceGateway;
import ru.bmstu.travel.core.gateway.ReferenceGateway;
import ru.bmstu.travel.core.gateway.ReferenceServiceException;
import ru.bmstu.travel.core.history.TravelAgencyHistoryObserver;
import ru.bmstu.travel.core.model.Booking;
import ru.bmstu.travel.core.repository.InMemoryBookingRepository;
import ru.bmstu.travel.core.service.BookingService;
import ru.bmstu.travel.core.service.ExpressTravelCalculatorGodClass;
import ru.bmstu.travel.proto.core.AddClientRequest;
import ru.bmstu.travel.proto.core.AddTourRequest;
import ru.bmstu.travel.proto.core.BookingResponse;
import ru.bmstu.travel.proto.core.CancelBookingRequest;
import ru.bmstu.travel.proto.core.ClientMutationResponse;
import ru.bmstu.travel.proto.core.CoreServiceGrpc;
import ru.bmstu.travel.proto.core.CreateBookingRequest;
import ru.bmstu.travel.proto.core.Empty;
import ru.bmstu.travel.proto.core.EstimateResponse;
import ru.bmstu.travel.proto.core.HistoryResponse;
import ru.bmstu.travel.proto.core.ListBookingsResponse;
import ru.bmstu.travel.proto.core.ListClientsResponse;
import ru.bmstu.travel.proto.core.ListToursResponse;
import ru.bmstu.travel.proto.core.QuickEstimateRequest;
import ru.bmstu.travel.proto.core.TourMutationResponse;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CoreGrpcService extends CoreServiceGrpc.CoreServiceImplBase {
    private static final Logger logger = Logger.getLogger(CoreGrpcService.class.getName());
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^C-\\d{3}$");
    private static final Pattern TOUR_ID_PATTERN = Pattern.compile("^T-\\d{3}$");
    private static final Pattern BOOKING_ID_PATTERN = Pattern.compile("^B-\\d{3}$");

    private final ReferenceGateway gateway;
    private final BookingService bookingService;
    private final ExpressTravelCalculatorGodClass estimator;

    public CoreGrpcService() {
        this(new GrpcReferenceGateway());
    }

    public CoreGrpcService(ReferenceGateway gateway) {
        this.gateway = gateway;
        var history = new TravelAgencyHistoryObserver();
        this.bookingService = new BookingService(new InMemoryBookingRepository(), gateway, null, null, history);
        this.estimator = new ExpressTravelCalculatorGodClass(gateway);
    }

    @Override
    public void listTours(Empty request, StreamObserver<ListToursResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ListTours");
        try {
            var tours = gateway.listTours(traceId).stream().map(this::toProtoTour).toList();
            responseObserver.onNext(ListToursResponse.newBuilder().setSuccess(true).setMessage("OK").addAllTours(tours).build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    @Override
    public void listClients(Empty request, StreamObserver<ListClientsResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ListClients");
        try {
            var clients = gateway.listClients(traceId).stream().map(this::toProtoClient).toList();
            responseObserver.onNext(ListClientsResponse.newBuilder().setSuccess(true).setMessage("OK").addAllClients(clients).build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    @Override
    public void addTour(AddTourRequest request, StreamObserver<TourMutationResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " AddTour title=" + request.getTitle());
        try {
            var result = gateway.addTour(request.getTitle(), request.getDestination(), request.getBasePrice(), request.getCapacity(), traceId);
            if (result.tour() != null) {
                bookingService.recordEvent(String.format("Добавлен тур %s (%s).", result.tour().id(), result.tour().title()));
            }
            responseObserver.onNext(TourMutationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(result.message())
                    .setTour(toProtoTour(result.tour()))
                    .build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    @Override
    public void addClient(AddClientRequest request, StreamObserver<ClientMutationResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " AddClient full_name=" + request.getFullName());
        try {
            CatalogDiscount discount = null;
            if (request.getHasDiscount()) {
                discount = new CatalogDiscount(
                        request.getDiscount().getName(),
                        request.getDiscount().getPercent(),
                        request.getDiscount().getType().name()
                );
            }
            var result = gateway.addClient(request.getFullName(), request.getPhone(), discount, traceId);
            if (result.client() != null) {
                bookingService.recordEvent(String.format("Добавлен клиент %s (%s).", result.client().id(), result.client().fullName()));
            }
            responseObserver.onNext(ClientMutationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(result.message())
                    .setClient(toProtoClient(result.client()))
                    .build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    @Override
    public void createBooking(CreateBookingRequest request, StreamObserver<BookingResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " CreateBooking client_id=" + request.getClientId() + " tour_id=" + request.getTourId());
        try {
            validateId(request.getClientId(), CLIENT_ID_PATTERN, "клиента", "C-001");
            validateId(request.getTourId(), TOUR_ID_PATTERN, "тура", "T-001");
            Booking booking = bookingService.createBooking(request.getClientId(), request.getTourId(), traceId);
            responseObserver.onNext(BookingResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Бронирование создано.")
                    .setBooking(toProtoBooking(booking))
                    .build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    @Override
    public void cancelBooking(CancelBookingRequest request, StreamObserver<BookingResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " CancelBooking booking_id=" + request.getBookingId());
        try {
            validateId(request.getBookingId(), BOOKING_ID_PATTERN, "бронирования", "B-001");
            Booking booking = bookingService.cancelBooking(request.getBookingId(), traceId);
            responseObserver.onNext(BookingResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Бронирование отменено.")
                    .setBooking(toProtoBooking(booking))
                    .build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    @Override
    public void listBookings(Empty request, StreamObserver<ListBookingsResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ListBookings");
        var bookings = bookingService.listBookings().stream().map(this::toProtoBooking).toList();
        responseObserver.onNext(ListBookingsResponse.newBuilder().addAllBookings(bookings).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listHistory(Empty request, StreamObserver<HistoryResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " ListHistory");
        responseObserver.onNext(HistoryResponse.newBuilder().addAllEvents(bookingService.getHistory()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void quickEstimate(QuickEstimateRequest request, StreamObserver<EstimateResponse> responseObserver) {
        String traceId = Tracing.currentTraceId();
        logger.info(() -> "trace_id=" + traceId + " QuickEstimate tour_id=" + request.getTourId() + " client_id=" + request.getClientId());
        try {
            validateId(request.getTourId(), TOUR_ID_PATTERN, "тура", "T-001");
            if (!request.getClientId().isBlank()) {
                validateId(request.getClientId(), CLIENT_ID_PATTERN, "клиента", "C-001");
            }
            var result = estimator.calculate(request, traceId);
            responseObserver.onNext(EstimateResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("OK")
                    .setTotal(result.total())
                    .setPrepayment(result.prepayment())
                    .setMonthlyPayment(result.monthlyPayment())
                    .setHeadline(result.headline())
                    .addAllExplanation(result.explanation())
                    .build());
            responseObserver.onCompleted();
        } catch (Throwable exc) {
            onDomainError(responseObserver, exc);
        }
    }

    private void validateId(String value, Pattern pattern, String entityName, String example) {
        if (!pattern.matcher(value).matches()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(String.format("Некорректный ID %s. Ожидаемый формат: %s.", entityName, example))
                    .asRuntimeException();
        }
    }

    private void onDomainError(StreamObserver<?> observer, Throwable exc) {
        if (exc instanceof StatusRuntimeException statusRuntimeException) {
            observer.onError(statusRuntimeException);
            return;
        }
        if (exc instanceof ReferenceServiceException referenceException) {
            observer.onError(Status.fromCode(referenceException.statusCode()).withDescription(referenceException.getMessage()).asRuntimeException());
            return;
        }
        if (exc instanceof InvalidDataException) {
            observer.onError(Status.INVALID_ARGUMENT.withDescription(exc.getMessage()).asRuntimeException());
            return;
        }
        if (exc instanceof NotFoundException) {
            observer.onError(Status.NOT_FOUND.withDescription(exc.getMessage()).asRuntimeException());
            return;
        }
        if (exc instanceof BookingStateException) {
            observer.onError(Status.FAILED_PRECONDITION.withDescription(exc.getMessage()).asRuntimeException());
            return;
        }
        if (exc instanceof TourAvailabilityException) {
            observer.onError(Status.RESOURCE_EXHAUSTED.withDescription(exc.getMessage()).asRuntimeException());
            return;
        }
        if (exc instanceof PricingException) {
            observer.onError(Status.FAILED_PRECONDITION.withDescription(exc.getMessage()).asRuntimeException());
            return;
        }
        if (exc instanceof TravelAgencyException) {
            observer.onError(Status.UNKNOWN.withDescription(exc.getMessage()).asRuntimeException());
            return;
        }
        logger.log(Level.SEVERE, "Unexpected error in CoreService", exc);
        observer.onError(Status.INTERNAL.withDescription("Внутренняя ошибка core-сервиса.").asRuntimeException());
    }

    private ru.bmstu.travel.proto.core.Discount toProtoDiscount(CatalogDiscount discount) {
        if (discount == null) {
            return ru.bmstu.travel.proto.core.Discount.newBuilder().build();
        }
        return ru.bmstu.travel.proto.core.Discount.newBuilder()
                .setName(discount.name())
                .setPercent(discount.percent())
                .setType(ru.bmstu.travel.proto.core.DiscountType.valueOf(discount.discountType()))
                .build();
    }

    private ru.bmstu.travel.proto.core.Client toProtoClient(CatalogClient client) {
        return ru.bmstu.travel.proto.core.Client.newBuilder()
                .setId(client.id())
                .setFullName(client.fullName())
                .setPhone(client.phone())
                .setHasDiscount(client.discount() != null)
                .setDiscount(toProtoDiscount(client.discount()))
                .build();
    }

    private ru.bmstu.travel.proto.core.Tour toProtoTour(CatalogTour tour) {
        return ru.bmstu.travel.proto.core.Tour.newBuilder()
                .setId(tour.id())
                .setTitle(tour.title())
                .setDestination(tour.destination())
                .setBasePrice(tour.basePrice())
                .setCapacity(tour.capacity())
                .setBookedSeats(tour.bookedSeats())
                .build();
    }

    private ru.bmstu.travel.proto.core.Booking toProtoBooking(Booking booking) {
        return ru.bmstu.travel.proto.core.Booking.newBuilder()
                .setId(booking.id())
                .setClientId(booking.clientId())
                .setClientName(booking.clientName())
                .setTourId(booking.tourId())
                .setTourTitle(booking.tourTitle())
                .setFinalPrice(booking.finalPrice())
                .setCreatedAt(booking.createdAt())
                .setStatus(booking.status().name())
                .build();
    }
}
