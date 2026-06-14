package ru.mins.parking.core.ui;

import ru.mins.parking.core.exception.ParkingException;
import ru.mins.parking.core.model.ParkingSession;
import ru.mins.parking.core.model.SpotType;
import ru.mins.parking.core.model.TariffType;
import ru.mins.parking.core.model.Vehicle;
import ru.mins.parking.core.model.VehicleType;
import ru.mins.parking.core.service.OccupancyReportService;
import ru.mins.parking.core.service.ParkingCoreService;
import ru.mins.parking.core.util.InputUtils;
import ru.mins.parking.core.util.TraceContext;
import ru.mins.parking.core.util.TraceIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleUi {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final ParkingCoreService parkingCoreService;
    private final OccupancyReportService reportService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUi(ParkingCoreService parkingCoreService, OccupancyReportService reportService) {
        this.parkingCoreService = parkingCoreService;
        this.reportService = reportService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                return;
            }
            String choice = scanner.nextLine().trim();
            try {
                runWithTrace(() -> {
                    switch (choice) {
                        case "1" -> addSpot();
                        case "2" -> showAllSpots();
                        case "3" -> showFreeSpots();
                        case "4" -> registerEntry();
                        case "5" -> registerExit();
                        case "6" -> showActiveSessions();
                        case "7" -> showReport();
                        case "0" -> { }
                        default -> System.out.println("Неизвестный пункт меню");
                    }
                });
                if ("0".equals(choice)) {
                    running = false;
                }
            } catch (ParkingException exception) {
                System.out.println(exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Непредвиденная ошибка: " + exception.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("Система управления парковкой");
        System.out.println("1. Добавить парковочное место");
        System.out.println("2. Показать все места");
        System.out.println("3. Показать свободные места");
        System.out.println("4. Зарегистрировать въезд");
        System.out.println("5. Зарегистрировать выезд");
        System.out.println("6. Показать активные сессии");
        System.out.println("7. Показать отчет по загрузке");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void addSpot() {
        System.out.print("Введите id места: ");
        int id = InputUtils.parsePositiveInt(scanner.nextLine(), "id места");
        SpotType spotType = InputUtils.readEnum(scanner, SpotType.class, "тип места");
        System.out.println(parkingCoreService.addParkingSpot(id, spotType));
    }

    private void showAllSpots() {
        parkingCoreService.getAllSpots().forEach(System.out::println);
    }

    private void showFreeSpots() {
        parkingCoreService.getFreeSpots().forEach(System.out::println);
    }

    private void registerEntry() {
        System.out.print("Введите номер автомобиля: ");
        String plate = InputUtils.normalizeText(scanner.nextLine(), "номер автомобиля").toUpperCase();
        VehicleType vehicleType = InputUtils.readEnum(scanner, VehicleType.class, "тип автомобиля");
        SpotType spotType = InputUtils.readEnum(scanner, SpotType.class, "тип места");
        TariffType tariffType = InputUtils.readEnum(scanner, TariffType.class, "тип тарифа");
        ParkingSession session = parkingCoreService.registerEntry(new Vehicle(plate, vehicleType), spotType, tariffType);
        System.out.println("Въезд зарегистрирован");
        System.out.println("Назначено место: #" + session.getParkingSpot().getId());
        System.out.println("Время въезда: " + session.getEntryTime().format(FORMATTER));
    }

    private void registerExit() {
        System.out.print("Введите номер автомобиля: ");
        String plate = InputUtils.normalizeText(scanner.nextLine(), "номер автомобиля").toUpperCase();
        ParkingSession session = parkingCoreService.registerExit(plate);
        System.out.println("Выезд зарегистрирован");
        System.out.println("Освобождено место: #" + session.getParkingSpot().getId());
        System.out.println("Стоимость: " + session.getCost());
    }

    private void showActiveSessions() {
        if (parkingCoreService.getActiveSessions().isEmpty()) {
            System.out.println("Активных сессий нет");
            return;
        }
        for (ParkingSession session : parkingCoreService.getActiveSessions()) {
            System.out.println(session.getVehicle().getLicensePlate()
                    + " | место #" + session.getParkingSpot().getId()
                    + " | въезд " + session.getEntryTime().format(FORMATTER)
                    + " | тариф " + session.getTariffType());
        }
    }

    private void showReport() {
        System.out.println(reportService.buildReport());
    }

    private void runWithTrace(Runnable action) {
        TraceContext.set(TraceIdGenerator.newTraceId());
        try {
            action.run();
        } finally {
            TraceContext.clear();
        }
    }
}
