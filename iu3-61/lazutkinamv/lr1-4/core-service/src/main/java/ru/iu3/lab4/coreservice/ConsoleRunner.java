package ru.iu3.lab4.coreservice;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.MDC;
import ru.iu3.lab4.coreservice.client.VehicleInfo;
import ru.iu3.lab4.proto.core.*;
import ru.iu3.lab4.coreservice.client.ReferenceServiceClient;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConsoleRunner {
    private static final String CORE_HOST = "localhost";
    private static final int CORE_PORT = 50051;
    private static final int REF_PORT = 50052;

    private final CoreServiceGrpc.CoreServiceBlockingStub coreStub;
    private final ReferenceServiceClient refClient;
    private final Scanner scanner;

    public ConsoleRunner() {
        // Канал к Core Service
        ManagedChannel coreChannel = ManagedChannelBuilder.forAddress(CORE_HOST, CORE_PORT)
                .usePlaintext()
                .build();
        this.coreStub = CoreServiceGrpc.newBlockingStub(coreChannel);

        // Канал к Reference Service
        ManagedChannel refChannel = ManagedChannelBuilder.forAddress(CORE_HOST, REF_PORT)
                .usePlaintext()
                .build();
        this.refClient = new ReferenceServiceClient(refChannel);

        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Транспортная компания ===");
        while (true) {
            String traceId = "ui-" + UUID.randomUUID().toString().substring(0, 8);
            MDC.put("traceId", traceId);

            try {
                printMenu();
                int choice = getIntInput("Выбор: ");

                try {
                    switch (choice) {
                        case 1 -> createOrder(traceId);
                        case 2 -> assignVehicle(traceId);
                        case 3 -> updateStatus(traceId);
                        case 4 -> showAllOrders(traceId);
                        case 5 -> showAllVehicles(traceId);
                        case 6 -> cancelOrder(traceId);
                        case 0 -> {
                            shutdown();
                            return;
                        }
                        default -> System.out.println("Неверный выбор!");
                    }
                } catch (StatusRuntimeException e) {
                    System.out.println("Ошибка связи с сервисом: " + e.getStatus().getDescription());
                } catch (Exception e) {
                    System.out.println("Системная ошибка: " + e.getMessage());
                }

            } finally {
                MDC.remove("traceId");
            }
        }
    }

    private void createOrder(String traceId) {
        System.out.print("Откуда: ");
        String from = scanner.nextLine();
        System.out.print("Куда: ");
        String to = scanner.nextLine();
        double weight = getDoubleInput("Вес (кг): ");

        CreateOrderRequest request = CreateOrderRequest.newBuilder()
                .setFrom(from)
                .setTo(to)
                .setWeight(weight)
                .setTraceId(traceId)
                .build();

        CreateOrderResponse response = coreStub
                .withDeadlineAfter(5, TimeUnit.SECONDS)
                .createOrder(request);

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
        } else {
            System.out.println("Заказ создан! ID: " + response.getOrderId());
        }
    }

    private void assignVehicle(String traceId) {
        String orderId = getInput("ID заказа: ");
        showAllVehicles(traceId);
        String vehicleId = getInput("ID транспорта: ");

        AssignVehicleRequest request = AssignVehicleRequest.newBuilder()
                .setOrderId(orderId)
                .setVehicleId(vehicleId)
                .setTraceId(traceId)
                .build();

        AssignVehicleResponse response = coreStub
                .withDeadlineAfter(5, TimeUnit.SECONDS)
                .assignVehicle(request);

        if (!response.getError().isEmpty()) {
            if (response.getError().contains("не найден") || response.getError().contains("недоступен")) {
                System.out.println(response.getError());
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        } else {
            System.out.println("Транспорт назначен!");
        }
    }

    private void updateStatus(String traceId) {
        String orderId = getInput("ID заказа: ");
        System.out.println("Доступные статусы: NEW, IN_PROGRESS, DELIVERED, CANCELLED");
        String statusStr = getInput("Новый статус: ");

        UpdateStatusRequest request = UpdateStatusRequest.newBuilder()
                .setOrderId(orderId)
                .setStatus(statusStr.trim().toUpperCase())
                .setTraceId(traceId)
                .build();

        UpdateStatusResponse response = coreStub
                .withDeadlineAfter(5, TimeUnit.SECONDS)
                .updateStatus(request);

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
        } else {
            System.out.println("Статус обновлен!");
        }
    }

    private void showAllOrders(String traceId) {
        System.out.println("\n=== ВСЕ ЗАКАЗЫ ===");

        GetAllOrdersRequest request = GetAllOrdersRequest.newBuilder()
                .setTraceId(traceId)
                .build();

        GetAllOrdersResponse response = coreStub
                .withDeadlineAfter(5, TimeUnit.SECONDS)
                .getAllOrders(request);

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
            return;
        }

        if (response.getOrdersList().isEmpty()) {
            System.out.println("Заказов нет");
            return;
        }

        for (OrderData o : response.getOrdersList()) {
            System.out.printf("ID: %s | %s → %s | %.2f кг | %s | Транспорт: %s | Цена: %.2f%n",
                    o.getOrderId(),
                    o.getFrom(),
                    o.getTo(),
                    o.getWeight(),
                    o.getStatus(),
                    o.getVehicleId().isEmpty() ? "не назначен" : o.getVehicleId(),
                    o.getPrice());
        }
    }

    private void showAllVehicles(String traceId) {
        System.out.println("\nДоступный транспорт:");
        List<VehicleInfo> vehicles = refClient.getAllVehicles(traceId);
        if (vehicles.isEmpty()) {
            System.out.println("Не удалось получить список транспорта (справочник недоступен)");
        } else {
            System.out.printf("%-6s | %-12s%n", "ID", "Вместимость");
            System.out.println("-------|-------------");
            for (VehicleInfo v : vehicles) {
                System.out.printf("%-6s 2| %.0f кг%n", v.id(), v.capacity());
            }
        }
    }

    private void cancelOrder(String traceId) {
        String orderId = getInput("ID заказа для отмены: ");

        CancelOrderRequest request = CancelOrderRequest.newBuilder()
                .setOrderId(orderId)
                .setTraceId(traceId)
                .build();

        CancelOrderResponse response = coreStub
                .withDeadlineAfter(5, TimeUnit.SECONDS)
                .cancelOrder(request);

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
        } else {
            System.out.println("Заказ отменен!");
        }
    }

    private void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Создать заказ");
        System.out.println("2. Назначить транспорт");
        System.out.println("3. Изменить статус заказа");
        System.out.println("4. Показать все заказы");
        System.out.println("5. Показать весь транспорт");
        System.out.println("6. Отменить заказ");
        System.out.println("0. Выход");
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    public void shutdown() {
        System.out.println("Завершение работы клиента...");
        refClient.shutdown();
    }

    public static void main(String[] args) {
        ConsoleRunner runner = new ConsoleRunner();
        runner.start();
    }
}