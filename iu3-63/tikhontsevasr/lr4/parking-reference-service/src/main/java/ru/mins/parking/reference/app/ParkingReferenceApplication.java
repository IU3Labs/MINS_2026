package ru.mins.parking.reference.app;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.mins.parking.reference.config.ReferenceServiceConfig;
import ru.mins.parking.reference.grpc.ParkingReferenceGrpcService;
import ru.mins.parking.reference.grpc.TraceServerInterceptor;
import ru.mins.parking.reference.repository.InMemoryReferenceDataRepository;
import ru.mins.parking.reference.repository.ReferenceDataRepository;
import ru.mins.parking.reference.service.ReferenceCatalogService;
import ru.mins.parking.reference.util.LogUtil;

import java.io.IOException;
import java.util.logging.Logger;

public class ParkingReferenceApplication {
    private static final Logger LOGGER = Logger.getLogger(ParkingReferenceApplication.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        ReferenceServiceConfig config = new ReferenceServiceConfig(9090);
        ReferenceDataRepository repository = new InMemoryReferenceDataRepository();
        ReferenceCatalogService catalogService = new ReferenceCatalogService(repository);

        Server server = ServerBuilder.forPort(config.port())
                .intercept(new TraceServerInterceptor())
                .addService(new ParkingReferenceGrpcService(catalogService))
                .build()
                .start();

        LogUtil.info(LOGGER, "Parking Reference Service запущен на порту " + config.port());
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
