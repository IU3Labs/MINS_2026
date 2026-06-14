package ru.bmstu.travel.core;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.bmstu.travel.common.Tracing;
import ru.bmstu.travel.core.gateway.GrpcReferenceGateway;

import java.io.IOException;
import java.util.logging.Logger;

public class CoreServer {
    private static final Logger logger = Logger.getLogger(CoreServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "127.0.0.1";
        int port = 50051;
        String referenceTarget = "127.0.0.1:50052";

        Server server = ServerBuilder.forPort(port)
                .intercept(Tracing.serverInterceptor())
                .addService(new CoreGrpcService(new GrpcReferenceGateway(referenceTarget)))
                .build()
                .start();

        logger.info("Core service started on " + host + ":" + port);
        logger.info("Core service uses reference service at " + referenceTarget);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
