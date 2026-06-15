package ru.bmstu.service_b;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ru.bmstu.grpc.*;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public class ReferenceServiceApp extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger log = Logger.getLogger("ReferenceService");

    private static final double DEFAULT_INTEREST_RATE = 7.5;
    private static final Set<String> BLOCKED_ACCOUNTS = Set.of("4", "5", "34");

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
    public void getAccountInfo(ValidationRequest req, StreamObserver<ValidationResponse> responseObserver) {
        String trace = req.getTraceId();
        String accountNumber = req.getAccountNumber();

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Номер счета не может быть пустым")
                            .asRuntimeException()
            );
            return;
        }

        log.info("[" + trace + "] Запрос информации для счета: " + accountNumber);

        boolean allowed = !BLOCKED_ACCOUNTS.contains(accountNumber);
        String msg = allowed ? "Счет действителен" : "Счет заблокирован";

        if (!allowed) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("Счет " + accountNumber + " заблокирован")
                            .asRuntimeException()
            );
            return;
        }

        ValidationResponse resp = ValidationResponse.newBuilder()
                .setTraceId(trace)
                .setIsAllowed(true)
                .setMessage(msg)
                .setInterestRate(DEFAULT_INTEREST_RATE)
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
        log.info("[" + trace + "] Ответ отправлен: " + msg);
    }
}