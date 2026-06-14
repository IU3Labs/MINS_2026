package ru.bmstu;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.bmstu.repository.ClientRepository;
import ru.bmstu.repository.TourRepository;
import ru.bmstu.server.ReferenceServiceImpl;
import ru.bmstu.util.Logger;
import ru.bmstu.util.TestDataInitializer;

import java.io.IOException;

public class ServiceBMain {

    private static final int PORT = 50051;

    public static void main(String[] args) {
        Logger.log("SERVICE-B", "Запуск Reference Service...");

        // Инициализация репозиториев
        TourRepository tourRepository = new TourRepository();
        ClientRepository clientRepository = new ClientRepository();

        // Загрузка тестовых данных
        TestDataInitializer testDataInitializer = new TestDataInitializer(tourRepository, clientRepository);
        testDataInitializer.initialize();

        // Создание gRPC сервера
        Server server = ServerBuilder.forPort(PORT)
                .addService(new ReferenceServiceImpl(tourRepository, clientRepository))
                .build();

        try {
            server.start();
            Logger.log("SERVICE-B", "gRPC сервер запущен на порту " + PORT);
            Logger.log("SERVICE-B", "Ожидание запросов...");

            // Добавляем shutdown hook для корректного завершения
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Logger.log("SERVICE-B", "Получен сигнал завершения, остановка сервера...");
                server.shutdown();
                Logger.log("SERVICE-B", "Сервер остановлен");
            }));

            // Блокируем основной поток, чтобы сервер работал
            server.awaitTermination();

        } catch (IOException e) {
            Logger.log("SERVICE-B", "Ошибка при запуске сервера: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Logger.log("SERVICE-B", "Сервер был прерван");
        }
    }
}