package com.bank.core;

import com.bank.core.grpc.CoreBankGrpcService;
import com.bank.core.reference.ReferenceDataClient;
import com.bank.core.repository.JsonAccountRepository;
import com.bank.core.service.CoreBankDomainService;
import com.bank.core.trace.TraceIdServerInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CoreServiceApplication {
    private static final Logger LOGGER = Logger.getLogger(CoreServiceApplication.class.getName());
    private static final int CORE_PORT = 6565;

    public static void main(String[] args) throws IOException, InterruptedException {
        ReferenceDataClient referenceDataClient = new ReferenceDataClient("127.0.0.1", 6566);
        JsonAccountRepository repository = new JsonAccountRepository("bank_data.json");
        CoreBankDomainService domainService = new CoreBankDomainService(repository, referenceDataClient);

        Server server = ServerBuilder.forPort(CORE_PORT)
                .addService(ServerInterceptors.intercept(new CoreBankGrpcService(domainService), new TraceIdServerInterceptor()))
                .build()
                .start();

        LOGGER.info("Core Service запущен на порту " + CORE_PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Остановка Core Service...");
            referenceDataClient.shutdown();
            try {
                server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        server.awaitTermination();
    }
}
