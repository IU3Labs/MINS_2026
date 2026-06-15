package org.parking.service_b;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.parking.grpc.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class ReferenceServiceApp extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger log = Logger.getLogger("ReferenceService");

    // Типа справочные данные
    private static final double BASE_RATE = 1.0;
    private static final Set<String> BLOCKED_PLATES = Set.of("BLOCKED1", "TEST999");

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                .addService(new ReferenceServiceApp())
                .build()
                .start();

        log.info("Service B (Reference) запущен на порту " + port);
        server.awaitTermination();
    }

    @Override
    public void getParkingRules(ValidationRequest req, StreamObserver<ValidationResponse> responseObserver) {
        String trace = req.getTraceId();
        String plate = req.getLicensePlate();

        //Статус 1: INVALID_ARGUMENT = неверные входные данные
        if (plate == null || plate.trim().isEmpty()) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Номер автомобиля не может быть пустым")
                            .asRuntimeException()
            );
            return;
        }

        log.info("[" + trace + "] Запрос правил для номера: " + plate);

        boolean allowed = !BLOCKED_PLATES.contains(plate);
        String msg = allowed ? "Разрешено" : "Номер в ЧС";

        // NOT_FOUND — номер не найден, в чс
        if (!allowed) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Номер " + plate + " отсутствует в базе разрешённых")
                            .asRuntimeException()
            );
            return;
        }

        ValidationResponse resp = ValidationResponse.newBuilder()
                .setTraceId(trace)
                .setIsAllowed(true)
                .setMessage(msg)
                .setHourlyRate(BASE_RATE)
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
        log.info("[" + trace + "] Ответ отправлен: " + msg);
    }
}