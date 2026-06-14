package ru.iu3.lab4.referenceservice.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.iu3.lab4.referenceservice.repository.CsvVehicleRepository;  // ← ИСПРАВЛЕНО
import ru.iu3.lab4.referenceservice.repository.VehicleRepository;
import ru.iu3.lab4.referenceservice.service.VehicleService;
import ru.iu3.lab4.referenceservice.service.VehicleServiceImpl;

import java.io.IOException;

public class ReferenceServer {
    private static final int PORT = 50052;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Запуск Reference Service на порту " + PORT);

        VehicleRepository vehicleRepo = new CsvVehicleRepository();
        VehicleService vehicleService = new VehicleServiceImpl(vehicleRepo);

        Server server = ServerBuilder.forPort(PORT)
                .addService(new ReferenceServiceImpl(vehicleService))
                .build()
                .start();

        System.out.println("Reference Service запущен и ждёт запросов...");
        System.out.println("Порт: " + PORT);
        System.out.println("Репозитории: Vehicle (CSV)");

        server.awaitTermination();
    }
}