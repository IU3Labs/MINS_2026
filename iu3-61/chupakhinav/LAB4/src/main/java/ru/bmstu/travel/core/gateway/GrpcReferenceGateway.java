package ru.bmstu.travel.core.gateway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import ru.bmstu.travel.common.Tracing;
import ru.bmstu.travel.core.catalog.CatalogClient;
import ru.bmstu.travel.core.catalog.CatalogDiscount;
import ru.bmstu.travel.core.catalog.CatalogTour;
import ru.bmstu.travel.core.catalog.ClientOperationResult;
import ru.bmstu.travel.core.catalog.TourOperationResult;
import ru.bmstu.travel.proto.reference.AddClientRequest;
import ru.bmstu.travel.proto.reference.AddTourRequest;
import ru.bmstu.travel.proto.reference.ClientRequest;
import ru.bmstu.travel.proto.reference.ReferenceServiceGrpc;
import ru.bmstu.travel.proto.reference.TourRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcReferenceGateway implements ReferenceGateway {
    private final String target;

    public GrpcReferenceGateway() {
        this("127.0.0.1:50052");
    }

    public GrpcReferenceGateway(String target) {
        this.target = target;
    }

    @Override
    public List<CatalogClient> listClients(String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var stub = stub(channel, traceId);
            var response = stub.listClients(ru.bmstu.travel.proto.reference.Empty.newBuilder().build());
            return response.getClientsList().stream().map(this::toClient).toList();
        } catch (StatusRuntimeException exc) {
            throwReferenceError("Справочный сервис недоступен. Невозможно получить список клиентов.", exc);
            return List.of();
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public List<CatalogTour> listTours(String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var stub = stub(channel, traceId);
            var response = stub.listTours(ru.bmstu.travel.proto.reference.Empty.newBuilder().build());
            return response.getToursList().stream().map(this::toTour).toList();
        } catch (StatusRuntimeException exc) {
            throwReferenceError("Справочный сервис недоступен. Невозможно получить список туров.", exc);
            return List.of();
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public CatalogClient getClient(String clientId, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var stub = stub(channel, traceId);
            var response = stub.getClient(ClientRequest.newBuilder().setClientId(clientId).build());
            return toClient(response.getClient());
        } catch (StatusRuntimeException exc) {
            if (exc.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return null;
            }
            throwReferenceError("Справочный сервис недоступен. Попробуйте позже.", exc);
            return null;
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public CatalogTour getTour(String tourId, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var stub = stub(channel, traceId);
            var response = stub.getTour(TourRequest.newBuilder().setTourId(tourId).build());
            return toTour(response.getTour());
        } catch (StatusRuntimeException exc) {
            if (exc.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return null;
            }
            throwReferenceError("Справочный сервис недоступен. Попробуйте позже.", exc);
            return null;
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public ClientOperationResult addClient(String fullName, String phone, CatalogDiscount discount, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var builder = AddClientRequest.newBuilder()
                    .setFullName(fullName)
                    .setPhone(phone)
                    .setHasDiscount(discount != null);
            if (discount != null) {
                builder.setDiscount(ru.bmstu.travel.proto.reference.Discount.newBuilder()
                        .setName(discount.name())
                        .setPercent(discount.percent())
                        .setType(ru.bmstu.travel.proto.reference.DiscountType.valueOf(discount.discountType()))
                        .build());
            }
            var response = stub(channel, traceId).addClient(builder.build());
            return new ClientOperationResult(response.getSuccess(), response.getMessage(), toClient(response.getClient()));
        } catch (StatusRuntimeException exc) {
            throwReferenceError("Справочный сервис недоступен. Невозможно добавить клиента.", exc);
            return null;
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public TourOperationResult addTour(String title, String destination, double basePrice, int capacity, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var request = AddTourRequest.newBuilder()
                    .setTitle(title)
                    .setDestination(destination)
                    .setBasePrice(basePrice)
                    .setCapacity(capacity)
                    .build();
            var response = stub(channel, traceId).addTour(request);
            return new TourOperationResult(response.getSuccess(), response.getMessage(), toTour(response.getTour()));
        } catch (StatusRuntimeException exc) {
            throwReferenceError("Справочный сервис недоступен. Невозможно добавить тур.", exc);
            return null;
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public TourOperationResult reserveTourSeat(String tourId, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var response = stub(channel, traceId).reserveTourSeat(TourRequest.newBuilder().setTourId(tourId).build());
            return new TourOperationResult(response.getSuccess(), response.getMessage(), toTour(response.getTour()));
        } catch (StatusRuntimeException exc) {
            throwReferenceError("Справочный сервис недоступен. Невозможно зарезервировать место в туре.", exc);
            return null;
        } finally {
            channel.shutdownNow();
        }
    }

    @Override
    public TourOperationResult releaseTourSeat(String tourId, String traceId) {
        ManagedChannel channel = newChannel();
        try {
            var response = stub(channel, traceId).releaseTourSeat(TourRequest.newBuilder().setTourId(tourId).build());
            return new TourOperationResult(response.getSuccess(), response.getMessage(), toTour(response.getTour()));
        } catch (StatusRuntimeException exc) {
            throwReferenceError("Справочный сервис недоступен. Невозможно освободить место в туре.", exc);
            return null;
        } finally {
            channel.shutdownNow();
        }
    }

    private ManagedChannel newChannel() {
        return ManagedChannelBuilder.forTarget(target).usePlaintext().build();
    }

    private ReferenceServiceGrpc.ReferenceServiceBlockingStub stub(ManagedChannel channel, String traceId) {
        return MetadataUtils.attachHeaders(ReferenceServiceGrpc.newBlockingStub(channel), Tracing.metadataWithTraceId(traceId))
                .withDeadlineAfter(2, TimeUnit.SECONDS);
    }

    private CatalogDiscount toDiscount(ru.bmstu.travel.proto.reference.Discount discount, boolean hasDiscount) {
        if (!hasDiscount) {
            return null;
        }
        return new CatalogDiscount(discount.getName(), discount.getPercent(), discount.getType().name());
    }

    private CatalogClient toClient(ru.bmstu.travel.proto.reference.Client client) {
        return new CatalogClient(
                client.getId(),
                client.getFullName(),
                client.getPhone(),
                toDiscount(client.getDiscount(), client.getHasDiscount())
        );
    }

    private CatalogTour toTour(ru.bmstu.travel.proto.reference.Tour tour) {
        return new CatalogTour(
                tour.getId(),
                tour.getTitle(),
                tour.getDestination(),
                tour.getBasePrice(),
                tour.getCapacity(),
                tour.getBookedSeats()
        );
    }

    private void throwReferenceError(String message, StatusRuntimeException exc) {
        Status.Code code = exc.getStatus().getCode();
        String details = exc.getStatus().getDescription();
        if (details == null || details.isBlank()) {
            details = message;
        }

        if (code == Status.Code.INVALID_ARGUMENT
                || code == Status.Code.NOT_FOUND
                || code == Status.Code.FAILED_PRECONDITION
                || code == Status.Code.RESOURCE_EXHAUSTED) {
            throw new ReferenceServiceException(details, code);
        }

        if (code == Status.Code.DEADLINE_EXCEEDED) {
            throw new ReferenceServiceException(message, Status.Code.DEADLINE_EXCEEDED);
        }

        throw new ReferenceServiceException(message, Status.Code.UNAVAILABLE);
    }
}
