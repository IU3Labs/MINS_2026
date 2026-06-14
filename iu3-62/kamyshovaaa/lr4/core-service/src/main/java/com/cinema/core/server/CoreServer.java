package com.cinema.core.server;

import com.cinema.core.util.TraceIdContext;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CoreServer {
    private static final Logger log = LoggerFactory.getLogger(CoreServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        TraceIdContext.setCurrentTraceId(TraceIdContext.generateShortUuid());
        int port = 8081;
        String refHost = "localhost";
        int refPort = 9090;

        log.info("Starting Core Service on port {} with Reference Service at {}:{}", port, refHost, refPort);

        CoreServiceImpl coreService = new CoreServiceImpl(refHost, refPort);

        Server server = ServerBuilder.forPort(port)
                .addService(coreService)
                .addService(ProtoReflectionService.newInstance()) // Для grpcurl
                .intercept(new TraceIdServerInterceptor())
                .build();

        server.start();
        log.info("Core Service started on port {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down Core Service");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}