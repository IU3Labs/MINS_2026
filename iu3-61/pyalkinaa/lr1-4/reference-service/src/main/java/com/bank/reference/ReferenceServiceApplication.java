package com.bank.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ReferenceServiceApplication {
    private static final Logger LOGGER = Logger.getLogger(ReferenceServiceApplication.class.getName());
    private static final int PORT = 6566;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(ServerInterceptors.intercept(new ReferenceServiceImpl(), new TraceIdServerInterceptor()))
                .build()
                .start();

        LOGGER.info("Reference Service запущен на порту " + PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Остановка Reference Service...");
            try {
                server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        server.awaitTermination();
    }
}
