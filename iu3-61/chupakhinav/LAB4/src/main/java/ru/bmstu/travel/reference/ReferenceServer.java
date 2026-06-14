package ru.bmstu.travel.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.bmstu.travel.common.Tracing;

import java.io.IOException;
import java.util.logging.Logger;

public class ReferenceServer {
    private static final Logger logger = Logger.getLogger(ReferenceServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "127.0.0.1";
        int port = 50052;

        Server server = ServerBuilder.forPort(port)
                .intercept(Tracing.serverInterceptor())
                .addService(new ReferenceGrpcService())
                .build()
                .start();

        logger.info("Reference service started on " + host + ":" + port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
