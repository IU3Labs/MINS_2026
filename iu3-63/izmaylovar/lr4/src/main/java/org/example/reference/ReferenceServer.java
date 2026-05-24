package org.example.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.infrastructure.grpc.TraceIdServerInterceptor;
import org.example.infrastructure.trace.ServiceLogger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ReferenceServer {
    private final int port;
    private final Server server;
    private final ServiceLogger logger = ServiceLogger.forComponent("reference-service", ReferenceServer.class);

    public ReferenceServer(int port, ReferenceGrpcService grpcService) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .intercept(new TraceIdServerInterceptor())
                .addService(grpcService)
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Reference gRPC server started on port " + port);
    }

    public void blockUntilShutdown() throws InterruptedException {
        server.awaitTermination();
    }

    public void stop() {
        logger.info("Stopping reference gRPC server");
        server.shutdown();
        try {
            if (!server.awaitTermination(3, TimeUnit.SECONDS)) {
                server.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            server.shutdownNow();
        }
    }
}
