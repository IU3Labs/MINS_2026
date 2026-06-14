package com.autoservice;

import com.autoservice.exception.AppointmentConflictException;
import com.autoservice.exception.AutoServiceException;
import com.autoservice.exception.ClientNotFoundException;
import com.autoservice.grpc.ReferenceServiceGrpcClient;
import com.autoservice.model.Appointment;
import com.autoservice.model.Car;
import com.autoservice.model.Client;
import com.autoservice.observer.ClientNotifierObserver;
import com.autoservice.observer.LoggerObserver;
import com.autoservice.repository.*;
import com.autoservice.repository.impl.*;
import com.autoservice.service.AutoServiceAppointmentService;
import com.autoservice.service.ClientManagementService;
import com.autoservice.service.ReportingService;
import com.autoservice.service.discount.*;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
/**
 * [Console]::InputEncoding = [System.Text.Encoding]::UTF8
 * [Console]::OutputEncoding = [System.Text.Encoding]::UTF8
 * mvn exec:java "-Dexec.mainClass=com.autoservice.ServiceBServer"
 * mvn exec:java "-Dexec.mainClass=com.autoservice.AutoServiceConsoleApp"
 * @see com.autoservice.ServiceBServer
 * @see com.autoservice.AutoServiceConsoleApp
 */

public class AutoServiceConsoleApp {
    private final ClientManagementService clientService;
    private final AutoServiceAppointmentService appointmentService;
    private final ReportingService reportingService;
    private final Scanner scanner;
    private final ReferenceServiceGrpcClient grpcClient;

    public AutoServiceConsoleApp(
            ClientRepository clientRepo,
            CarRepository carRepo,
            AppointmentRepository appointmentRepo,
            HistoryRepository historyRepo,
            ReferenceServiceGrpcClient grpcClient) {
        initializeData(clientRepo, carRepo);
        this.grpcClient = grpcClient;
        this.clientService = new ClientManagementService(clientRepo, carRepo);
        this.appointmentService = new AutoServiceAppointmentService(carRepo, appointmentRepo, historyRepo, grpcClient);
        this.reportingService = new ReportingService(appointmentRepo, historyRepo);
        this.scanner = new Scanner(System.in);
    }

    private void initializeData(ClientRepository clientRepo, CarRepository carRepo) {
        Client client1 = new Client("Иван Петров", "+7(999)123-45-67", "ivan@mail.ru");
        Client client2 = new Client("Мария Сидорова", "+7(999)234-56-78", "maria@mail.ru");
        Client client3 = new Client("Алексей Иванов", "+7(999)345-67-89", "alexey@mail.ru");

        clientRepo.addClient(client1);
        clientRepo.addClient(client2);
        clientRepo.addClient(client3);

        Car car1 = new Car("А123ВС77", "Toyota", "Camry", 2018, client1);
        Car car2 = new Car("В456ЕК99", "Hyundai", "Solaris", 2020, client1);
        Car car3 = new Car("С789МР50", "Kia", "Rio", 2019, client2);
        Car car4 = new Car("Х012НТ77", "Lada", "Vesta", 2021, client3);

        carRepo.addCar(car1);
        carRepo.addCar(car2);
        carRepo.addCar(car3);
        carRepo.addCar(car4);

    }


    public void run() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        createAppointment();
                        break;
                    case "2":
                        completeAppointment();
                        break;
                    case "3":
                        updateAppointmentStatus();
                        break;
                    case "4":
                        cancelAppointment();
                        break;
                    case "5":
                        reportingService.printTodaySchedule(getCurrentDate());
                        break;
                    case "6":
                        reportingService.printWorkHistory();
                        break;
                    case "7":
                        reportingService.calculateTotalEarnings();
                        break;
                    case "8":
                        registerNewClient();
                        break;
                    case "9":
                        registerNewCar();
                        break;
                    case "10":
                        listAllClients();
                        break;
                    case "11":
                        listAllAppointments();
                        break;
                    case "12":
                        System.out.println("Быстрый калькулятор временно недоступен");
                        break;
                    case "0":
                        System.out.println("До свидания!");
                        try {
                            grpcClient.shutdown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (AutoServiceException e) {
                System.out.println("ОШИБКА: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ОШИБКА: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }

    private void printMenu() {
        System.out.println("\n=== АВТОСЕРВИС ===");
        System.out.println("1. Записать автомобиль на ремонт");
        System.out.println("2. Отметить выполнение работы");
        System.out.println("3. Перевести заявку на следующий статус");
        System.out.println("4. Отменить заявку");
        System.out.println("5. Показать расписание на сегодня");
        System.out.println("6. Показать историю работ");
        System.out.println("7. Показать общую выручку");
        System.out.println("8. Зарегистрировать нового клиента");
        System.out.println("9. Зарегистрировать новый автомобиль");
        System.out.println("10. Список всех клиентов");
        System.out.println("11. Список всех записей");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void createAppointment() throws AutoServiceException {
        System.out.print("Введите госномер автомобиля: ");
        String plate = scanner.nextLine();
        System.out.print("Введите название услуги: ");
        String serviceName = scanner.nextLine();
        System.out.print("Введите дату и время (ГГГГ-ММ-ДД ЧЧ:ММ): ");
        String dateTime = scanner.nextLine();

        String traceId = UUID.randomUUID().toString();
        System.out.println("[TraceID: " + traceId + "] Отправка запроса к сервису справочников");

        Appointment appointment = null;
        try {
            appointment = appointmentService.createAppointment(plate, serviceName, dateTime, traceId);
        } catch (AppointmentConflictException e) {
            System.out.println("ОШИБКА: Конфликт записи - " + e.getMessage());
            return;
        }

        System.out.println("\nВыберите тип расчета:");
        System.out.println("1. Обычный (без скидок)");
        System.out.println("2. Для постоянных клиентов (скидка 5%)");
        System.out.println("3. Срочный (наценка 20%)");
        System.out.print("Ваш выбор: ");
        String discountChoice = scanner.nextLine();

        DiscountStrategy strategy;
        switch (discountChoice) {
            case "2":
                strategy = new RegularClientDiscountStrategy();
                break;
            case "3":
                strategy = new UrgentMarkupStrategy();
                break;
            default:
                strategy = new NoDiscountStrategy();
        }

        appointment.setDiscountStrategy(strategy);

        appointment.addObserver(new LoggerObserver());
        appointment.addObserver(new ClientNotifierObserver());

        System.out.println("Запись успешно создана! Стоимость: " + appointment.calculateCost() + " руб.");
    }

    private void completeAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointments()
                .stream()
                .filter(a -> !a.isCompleted() && !a.isCancelled())
                .collect(java.util.stream.Collectors.toList());

        if (appointments.isEmpty()) {
            System.out.println("Нет незавершенных записей");
            return;
        }

        System.out.println("Доступные записи:");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". " + appointments.get(i));
        }

        System.out.print("Выберите номер записи для завершения: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice >= 0 && choice < appointments.size()) {
            Appointment selected = appointments.get(choice);
            appointmentService.completeAppointment(selected);
            System.out.println("Работа отмечена как выполненная!");
        }
    }

    private void updateAppointmentStatus() {
        List<Appointment> appointments = appointmentService.getAllAppointments()
                .stream()
                .filter(a -> !a.isCompleted() && !a.isCancelled())
                .collect(java.util.stream.Collectors.toList());

        if (appointments.isEmpty()) {
            System.out.println("Нет активных записей");
            return;
        }

        System.out.println("Активные записи:");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". " + appointments.get(i));
        }

        System.out.print("Выберите номер записи: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice >= 0 && choice < appointments.size()) {
            Appointment selected = appointments.get(choice);
            selected.nextStatus();
            System.out.println("Статус обновлен!");
        }
    }

    private void cancelAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointments()
                .stream()
                .filter(a -> !a.isCompleted() && !a.isCancelled())
                .collect(java.util.stream.Collectors.toList());

        if (appointments.isEmpty()) {
            System.out.println("Нет активных записей");
            return;
        }

        System.out.println("Активные записи:");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". " + appointments.get(i));
        }

        System.out.print("Выберите номер записи для отмены: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;

        if (choice >= 0 && choice < appointments.size()) {
            Appointment selected = appointments.get(choice);
            selected.cancel();
            System.out.println("Заявка отменена!");
        }
    }

    private void registerNewClient() {
        System.out.print("Введите имя клиента: ");
        String name = scanner.nextLine();
        System.out.print("Введите телефон: ");
        String phone = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        clientService.registerClient(name, phone, email);
        System.out.println("Клиент зарегистрирован!");
    }

    private void registerNewCar() throws ClientNotFoundException {
        System.out.print("Введите госномер: ");
        String plate = scanner.nextLine();
        System.out.print("Введите марку: ");
        String brand = scanner.nextLine();
        System.out.print("Введите модель: ");
        String model = scanner.nextLine();
        System.out.print("Введите год выпуска: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите телефон владельца: ");
        String phone = scanner.nextLine();

        clientService.registerCar(plate, brand, model, year, phone);
        System.out.println("Автомобиль зарегистрирован!");
    }

    private void listAllClients() {
        System.out.println("\n=== КЛИЕНТЫ ===");
        clientService.getAllClients().forEach(System.out::println);
    }

    private void listAllAppointments() {
        System.out.println("\n=== ВСЕ ЗАПИСИ ===");
        appointmentService.getAllAppointments().forEach(System.out::println);
    }

    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }

    public static void main(String[] args) {
        ClientRepository clientRepo = new InMemoryClientRepository();
        CarRepository carRepo = new InMemoryCarRepository();
        AppointmentRepository appointmentRepo = new InMemoryAppointmentRepository();
        HistoryRepository historyRepo = new InMemoryHistoryRepository();

        ReferenceServiceGrpcClient grpcClient = new ReferenceServiceGrpcClient("localhost", 50051);

        AutoServiceConsoleApp app = new AutoServiceConsoleApp(
                clientRepo, carRepo, appointmentRepo, historyRepo, grpcClient);
        app.run();
    }
}