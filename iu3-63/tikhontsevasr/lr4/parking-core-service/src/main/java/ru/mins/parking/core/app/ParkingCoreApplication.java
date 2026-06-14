package ru.mins.parking.core.app;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.mins.parking.core.config.CoreServiceConfig;
import ru.mins.parking.core.grpc.ReferenceServiceClient;
import ru.mins.parking.core.model.SpotType;
import ru.mins.parking.core.repository.InMemoryParkingSessionRepository;
import ru.mins.parking.core.repository.InMemoryParkingSpotRepository;
import ru.mins.parking.core.repository.ParkingSessionRepository;
import ru.mins.parking.core.repository.ParkingSpotRepository;
import ru.mins.parking.core.service.OccupancyReportService;
import ru.mins.parking.core.service.ParkingCoreService;
import ru.mins.parking.core.ui.ConsoleUi;
import ru.mins.parking.core.util.DemoDataInitializer;

public class ParkingCoreApplication {
    public static void main(String[] args) {
        CoreServiceConfig config = new CoreServiceConfig("localhost", 9090, 80.0);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(config.referenceHost(), config.referencePort())
                .usePlaintext()
                .build();

        ParkingSpotRepository spotRepository = new InMemoryParkingSpotRepository();
        ParkingSessionRepository sessionRepository = new InMemoryParkingSessionRepository();
        ReferenceServiceClient referenceServiceClient = new ReferenceServiceClient(channel);
        ParkingCoreService parkingCoreService = new ParkingCoreService(
                spotRepository,
                sessionRepository,
                referenceServiceClient,
                config.highOccupancyThreshold()
        );
        OccupancyReportService reportService = new OccupancyReportService(spotRepository, sessionRepository);

        new DemoDataInitializer(parkingCoreService).populate(
                SpotType.REGULAR,
                SpotType.DISABLED,
                SpotType.ELECTRIC
        );

        try {
            new ConsoleUi(parkingCoreService, reportService).start();
        } finally {
            channel.shutdownNow();
        }
    }
}
