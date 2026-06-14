package com.university.server;

import com.university.client.ReferenceServiceClient;
import com.university.service.DiscountConfig;
import com.university.service.GradeAverageCalculator;
import com.university.service.QuickDiscountCalculator;
import com.university.service.observer.NotificationService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CoreServer {
    private static final int PORT = 50051;
    private static final String REF_HOST = "localhost";
    private static final int REF_PORT = 50052;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Запуск Core Service на порту " + PORT);

        ManagedChannel refChannel = ManagedChannelBuilder
                .forAddress(REF_HOST, REF_PORT)
                .usePlaintext()
                .build();
        ReferenceServiceClient refClient = new ReferenceServiceClient(refChannel);

        DiscountConfig config = new DiscountConfig();
        NotificationService notificationService = new NotificationService();

        QuickDiscountCalculator discountCalc = new QuickDiscountCalculator(
                config
        );

        Server server = ServerBuilder.forPort(PORT)
                .addService(new CoreServiceImpl(refClient, discountCalc, notificationService))
                .build()
                .start();

        System.out.println("Core Service запущен на порту " + PORT);
        System.out.println("Reference Service: " + REF_HOST + ":" + REF_PORT);

        server.awaitTermination();
    }
}
