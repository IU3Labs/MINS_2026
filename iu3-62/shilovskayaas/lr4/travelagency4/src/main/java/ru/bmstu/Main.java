package ru.bmstu;

import ru.bmstu.grpc.ReferenceServiceClient;
import ru.bmstu.repository.BookingRepository;
import ru.bmstu.service.*;
import ru.bmstu.service.interfaces.*;
import ru.bmstu.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {

        // Инициализация gRPC клиента для Service B
        ReferenceServiceClient refClient = new ReferenceServiceClient("localhost", 50051);

        // Репозитории (теперь только для бронирований, не для туров и клиентов!)
        BookingRepository bookingRepository = new BookingRepository();

        // Сервисы с передачей gRPC клиента
        IAdminService adminService = new AdminService(refClient, bookingRepository);

        IBookingService bookingService = new BookingService(bookingRepository, refClient);

        IClientAuthService clientAuthService = new ClientAuthService(refClient);

        IPaymentService paymentService = new PaymentService(bookingRepository);

        // Тестовые данные теперь инициализируются через Service B отдельно
        // TestDataInitializer больше не нужен в Service A
        System.out.println("Service A: Core Service запущен");
        System.out.println("Убедитесь, что Service B (Reference Service) запущен на порту 50051");
        System.out.println("Для просмотра туров и клиентов Service B должен быть активен\n");

        ConsoleUI ui = new ConsoleUI(adminService, bookingService, clientAuthService, paymentService);
        ui.start();

        // Закрываем соединение при завершении
        refClient.shutdown();
    }
}