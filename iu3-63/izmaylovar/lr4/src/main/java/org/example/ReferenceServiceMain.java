package org.example;

import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.inmemory.InMemoryHallRepository;
import org.example.domain.repository.inmemory.InMemoryMovieRepository;
import org.example.domain.repository.inmemory.InMemorySessionRepository;
import org.example.infrastructure.trace.ServiceLogger;
import org.example.reference.ReferenceGrpcService;
import org.example.reference.ReferenceServer;
import org.example.service.HallService;
import org.example.service.MovieService;

public class ReferenceServiceMain {
    private static final ServiceLogger LOGGER = ServiceLogger.forComponent("reference-service", ReferenceServiceMain.class);

    public static void main(String[] args) {
        int port = Integer.getInteger("reference.port", 9090);
        SessionRepository emptySessionRepository = new InMemorySessionRepository();
        MovieService movieService = new MovieService(new InMemoryMovieRepository(), emptySessionRepository);
        HallService hallService = new HallService(new InMemoryHallRepository(), emptySessionRepository);
        ReferenceServer server = new ReferenceServer(port, new ReferenceGrpcService(movieService, hallService));

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        try {
            server.start();
            LOGGER.info("Reference service is ready");
            server.blockUntilShutdown();
        } catch (Exception e) {
            LOGGER.error("Reference service failed to start: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
