package com.cinema.reference.server;

import com.cinema.reference.entity.*;
import com.cinema.reference.repository.Repository;
import com.cinema.reference.repository.impls.*;
import com.cinema.reference.service.CrudService;
import com.cinema.reference.service.impls.*;
import com.cinema.reference.service.uow.IUnitOfWork;
import com.cinema.reference.service.uow.IUnitOfWorkFactory;
import com.cinema.reference.service.uow.UnitOfWorkFactory;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReferenceServer {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServer.class);

    public static void main(String[] args) throws Exception {
        int port = 9090;

        // ========== 1. ТОЛЬКО РЕПОЗИТОРИИ (старые) ==========
        Repository<Movie> movieRepo = new MovieRepositoryImpl();
        Repository<Hall> hallRepo = new HallRepositoryImpl();
        Repository<Screening> screeningRepo = new ScreeningRepositoryImpl();

        CrudService<Movie> movieCrud = new CrudServiceImpl<>(movieRepo);
        CrudService<Hall> hallCrud = new CrudServiceImpl<>(hallRepo);
        CrudService<Screening> screeningCrud = new CrudServiceImpl<>(screeningRepo);

        // ========== 2. СТАРЫЕ СЕРВИСЫ (они сами создают CrudService внутри) ==========
        var movieService = new MovieServiceImpl(movieCrud, null);
        var hallService = new HallServiceImpl(hallCrud, null);

        IUnitOfWorkFactory uowFactory = new UnitOfWorkFactory();
        IUnitOfWork<Screening> screeningUow = uowFactory.create(screeningCrud);

        var screeningService = new ScreeningServiceImpl(screeningCrud, movieService, hallService, screeningUow);

        // ========== 3. ПЕРЕСОЗДАЁМ С ПРАВИЛЬНЫМИ ЗАВИСИМОСТЯМИ ==========
        var movieServiceFinal = new MovieServiceImpl(movieCrud, screeningService);
        var hallServiceFinal = new HallServiceImpl(hallCrud, screeningService);

        var screeningServiceFinal = new ScreeningServiceImpl(screeningCrud, movieServiceFinal,
                hallServiceFinal, screeningUow);

        // ========== 4. gRPC-АДАПТЕР (принимает СТАРЫЕ СЕРВИСЫ) ==========
        var grpcService = new ReferenceServiceImpl(movieServiceFinal, hallServiceFinal, screeningServiceFinal);

        // ========== 5. gRPC СЕРВЕР ==========
        Server server = ServerBuilder.forPort(port)
                .addService(grpcService)
                .addService(ProtoReflectionService.newInstance())
                .intercept(new TraceIdServerInterceptor())
                .build();

        server.start();
        log.info("Reference Service started on port {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down Reference Service");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}