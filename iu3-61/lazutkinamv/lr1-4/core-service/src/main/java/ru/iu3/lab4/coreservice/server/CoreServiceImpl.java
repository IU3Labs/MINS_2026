package ru.iu3.lab4.coreservice.server;

import io.grpc.stub.StreamObserver;
import ru.iu3.lab4.coreservice.client.ReferenceServiceClient;
import ru.iu3.lab4.coreservice.model.Order;
import ru.iu3.lab4.coreservice.model.OrderStatus;
import ru.iu3.lab4.coreservice.service.OrderService;
import ru.iu3.lab4.proto.core.*;

import java.util.List;
import java.util.UUID;

/**
 * Реализация gRPC-сервера для Core Service
 */
public class CoreServiceImpl extends CoreServiceGrpc.CoreServiceImplBase {
    private final OrderService orderService;
    private final ReferenceServiceClient refClient;

    public CoreServiceImpl(OrderService orderService, ReferenceServiceClient refClient) {
        this.orderService = orderService;
        this.refClient = refClient;
    }

    private String getOrGenerateTraceId(String provided) {
        return provided.isEmpty() ? "trace-" + UUID.randomUUID().toString().substring(0, 8) : provided;
    }

    @Override
    public void createOrder(CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] createOrder - ПОЛУЧЕН ЗАПРОС");

        var builder = CreateOrderResponse.newBuilder();
        try {
            System.out.println("[Trace:" + traceId + "] [CoreService] Вызываю orderService.createOrder...");
            Order order = orderService.createOrder(request.getFrom(), request.getTo(), request.getWeight());
            System.out.println("[Trace:" + traceId + "] [CoreService] Заказ создан: " + order.getId());
            builder.setOrderId(order.getId());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            System.out.println("[Trace:" + traceId + "] [CoreService] createOrder - ЗАВЕРШЕНО");
        } catch (Exception e) {
            System.err.println("[Trace:" + traceId + "] [CoreService] ОШИБКА в createOrder: " + e.getMessage());
            e.printStackTrace(); // ← ВАЖНО! Выводим полный stack trace
            builder.setError(e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void assignVehicle(AssignVehicleRequest request, StreamObserver<AssignVehicleResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] assignVehicle");

        var builder = AssignVehicleResponse.newBuilder();
        try {
            // Проверяем транспорт в Reference Service
            if (!refClient.validateVehicle(request.getVehicleId(), traceId)) {
                builder.setError("Транспорт не найден в справочнике");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            orderService.assignVehicle(request.getOrderId(), request.getVehicleId());
            builder.setSuccess(true);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            builder.setError(e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateStatus(UpdateStatusRequest request, StreamObserver<UpdateStatusResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] updateStatus");

        var builder = UpdateStatusResponse.newBuilder();
        try {
            OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
            orderService.updateStatus(request.getOrderId(), status);
            builder.setSuccess(true);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            builder.setError(e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllOrders(GetAllOrdersRequest request, StreamObserver<GetAllOrdersResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] getAllOrders");

        var builder = GetAllOrdersResponse.newBuilder();
        try {
            List<Order> orders = orderService.getAllOrders();
            for (Order o : orders) {
                builder.addOrders(OrderData.newBuilder()
                        .setOrderId(o.getId())
                        .setFrom(o.getFrom())
                        .setTo(o.getTo())
                        .setWeight(o.getWeight())
                        .setStatus(o.getStatus().name())
                        .setVehicleId(o.getVehicleId() != null ? o.getVehicleId() : "")
                        .setPrice(o.getPrice())
                        .build());
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            builder.setError(e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void calculatePrice(CalculatePriceRequest request, StreamObserver<CalculatePriceResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] calculatePrice");

        var builder = CalculatePriceResponse.newBuilder();
        try {
            double price = orderService.calculatePrice(request.getOrderId());
            builder.setPrice(price);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            builder.setError(e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void cancelOrder(CancelOrderRequest request, StreamObserver<CancelOrderResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] cancelOrder");

        var builder = CancelOrderResponse.newBuilder();
        try {
            orderService.cancelOrder(request.getOrderId());
            builder.setSuccess(true);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            builder.setError(e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }
}