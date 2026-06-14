package ru.iu3.lab4.coreservice.server;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.iu3.lab4.coreservice.client.ReferenceServiceClient;
import ru.iu3.lab4.coreservice.observer.OrderFileUpdateObserver;
import ru.iu3.lab4.coreservice.pricing.PriorityPricingStrategy;
import ru.iu3.lab4.coreservice.pricing.WeightBasedPricingStrategy;
import ru.iu3.lab4.coreservice.repository.CsvOrderRepository;
import ru.iu3.lab4.coreservice.repository.OrderRepository;
import ru.iu3.lab4.coreservice.service.OrderServiceImpl;

import java.io.IOException;

public class CoreServer {
    private static final int PORT = 50051;
    private static final String REF_HOST = "localhost";
    private static final int REF_PORT = 50052;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Запуск Core Service на порту " + PORT);

        // Создаем канал для связи с Reference Service
        ManagedChannel refChannel = ManagedChannelBuilder.forAddress(REF_HOST, REF_PORT)
                .usePlaintext()
                .build();

        ReferenceServiceClient refClient = new ReferenceServiceClient(refChannel);

        // Инициализируем зависимости вручную
        OrderRepository orderRepo = new CsvOrderRepository();
        WeightBasedPricingStrategy weightStrategy = new WeightBasedPricingStrategy();
        OrderFileUpdateObserver fileObserver = new OrderFileUpdateObserver(orderRepo);

        OrderServiceImpl orderService = new OrderServiceImpl(
                orderRepo,
                weightStrategy,
                refClient,
                fileObserver
        );

        Server server = ServerBuilder.forPort(PORT)
                .addService(new CoreServiceImpl(orderService, refClient))
                .build()
                .start();

        System.out.println("Core Service запущен на порту " + PORT);
        System.out.println("Reference Service: " + REF_HOST + ":" + REF_PORT);

        server.awaitTermination();
    }
}