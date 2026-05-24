package org.example.integration.reference;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.inmemory.InMemoryHallRepository;
import org.example.domain.repository.inmemory.InMemoryMovieRepository;
import org.example.domain.repository.inmemory.InMemorySessionRepository;
import org.example.infrastructure.grpc.TraceIdServerInterceptor;
import org.example.infrastructure.trace.TraceContext;
import org.example.reference.ReferenceGrpcService;
import org.example.service.HallService;
import org.example.service.MovieService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReferenceServiceClientTest {
    @AfterEach
    void clearTraceContext() {
        TraceContext.clear();
    }

    @Test
    void callsAfterInitialDeadlineWindowStillUseFreshDeadline() throws Exception {
        Server server = startReferenceServer(0);
        ReferenceServiceClient client = new ReferenceServiceClient("localhost", server.getPort());

        try {
            client.createMovie("Interstellar", 169, "Sci-Fi", "12+");

            Thread.sleep(TimeUnit.SECONDS.toMillis(4));

            assertEquals(1, client.getAllMovies().size());
        } finally {
            client.close();
            stop(server);
        }
    }

    @Test
    void clientCanConnectAfterReferenceServiceStartsLater() throws Exception {
        int port = freePort();
        ReferenceServiceClient client = new ReferenceServiceClient("localhost", port);

        assertThrows(ReferenceServiceUnavailableException.class, client::getAllMovies);

        Server server = startReferenceServer(port);
        try {
            client.createMovie("Dune", 155, "Sci-Fi", "12+");

            assertEquals(1, client.getAllMovies().size());
        } finally {
            client.close();
            stop(server);
        }
    }

    private Server startReferenceServer(int port) throws IOException {
        SessionRepository sessionRepository = new InMemorySessionRepository();
        MovieService movieService = new MovieService(new InMemoryMovieRepository(), sessionRepository);
        HallService hallService = new HallService(new InMemoryHallRepository(), sessionRepository);

        return ServerBuilder.forPort(port)
                .intercept(new TraceIdServerInterceptor())
                .addService(new ReferenceGrpcService(movieService, hallService))
                .build()
                .start();
    }

    private int freePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private void stop(Server server) throws InterruptedException {
        server.shutdown();
        if (!server.awaitTermination(3, TimeUnit.SECONDS)) {
            server.shutdownNow();
        }
    }
}
