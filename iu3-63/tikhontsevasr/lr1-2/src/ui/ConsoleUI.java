package ui;

import exception.ParkingException;
import model.ParkingSession;
import model.ParkingSpot;
import model.SpotType;
import model.Vehicle;
import model.VehicleType;
import report.OccupancyReportService;
import service.ParkingService;
import tariff.TariffStrategy;
import tariff.TariffType;
import util.InputUtils;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleUI {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final ParkingService parkingService;
    private final OccupancyReportService occupancyReportService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(ParkingService parkingService, OccupancyReportService occupancyReportService) {
        this.parkingService = parkingService;
        this.occupancyReportService = occupancyReportService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                break;
            }
            String choice = scanner.nextLine().trim();
            if (choice.isEmpty()) {
                continue;
            }
            try {
                switch (choice) {
                    case "1" : addSpot(); break;
                    case "2" : showAllSpots(); break;
                    case "3" : showFreeSpots(); break;
                    case "4" : registerEntry(); break;
                    case "5" : registerExit(); break;
                    case "6" : showActiveSessions(); break;
                    case "7" : showReport(); break;
                    case "0" : running = false; break;
                    default : System.out.println("Неизвестный пункт меню."); break;
                }
            } catch (ParkingException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Непредвиденная ошибка: " + exception.getMessage());
            }

            if (running) {
                System.out.println();
                System.out.println("Нажмите Enter, чтобы продолжить...");
                if (!scanner.hasNextLine()) {
                    break;
                }
                scanner.nextLine();
            }
        }
        System.out.println("Работа программы завершена.");
    }

    private void printMenu() {
        System.out.println("Информационная система парковки");
        System.out.println("1. Добавить машиноместо");
        System.out.println("2. Показать все машиноместа");
        System.out.println("3. Показать свободные места");
        System.out.println("4. Зарегистрировать въезд автомобиля");
        System.out.println("5. Зарегистрировать выезд автомобиля");
        System.out.println("6. Показать активные парковочные сессии");
        System.out.println("7. Сформировать отчет по загрузке");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private void addSpot() {
        System.out.print("Введите id машиноместа: ");
        int id = InputUtils.parsePositiveInt(scanner.nextLine(), "ID машиноместа");
        SpotType type = readSpotType();
        ParkingSpot spot = parkingService.addParkingSpot(id, type);
        System.out.println("Машиноместо добавлено: " + spot);
    }

    private void showAllSpots() {
        System.out.println("Все машиноместа:");
        parkingService.getAllSpots().forEach(System.out::println);
    }

    private void showFreeSpots() {
        System.out.println("Свободные машиноместа:");
        parkingService.getFreeSpots().forEach(System.out::println);
    }

    private void registerEntry() {
        System.out.print("Введите номер автомобиля: ");
        String plate = InputUtils.normalizeText(scanner.nextLine(), "Номер автомобиля").toUpperCase();
        VehicleType vehicleType = readVehicleType();
        SpotType preferredSpotType = readSpotType();
        TariffStrategy tariffStrategy = readTariffStrategy();

        ParkingSession session = parkingService.registerEntry(
                new Vehicle(plate, vehicleType),
                preferredSpotType,
                tariffStrategy
        );

        System.out.println("Въезд зарегистрирован.");
        System.out.println("Назначено место: #" + session.getParkingSpot().getId());
        System.out.println("Время въезда: " + session.getEntryTime().format(FORMATTER));
    }

    private void registerExit() {
        System.out.print("Введите номер автомобиля: ");
        String plate = InputUtils.normalizeText(scanner.nextLine(), "Номер автомобиля").toUpperCase();
        ParkingSession session = parkingService.registerExit(plate);
        System.out.println("Выезд зарегистрирован.");
        System.out.println("Освобождено место: #" + session.getParkingSpot().getId());
        System.out.println("Время выезда: " + session.getExitTime().format(FORMATTER));
        System.out.println("Стоимость парковки: " + session.getCost() + " руб.");
    }

    private void showActiveSessions() {
        System.out.println("Активные парковочные сессии:");
        if (parkingService.getActiveSessions().isEmpty()) {
            System.out.println("Активных сессий нет.");
            return;
        }
        for (ParkingSession session : parkingService.getActiveSessions()) {
            System.out.println(session.getVehicle().getLicensePlate()
                    + " | место #" + session.getParkingSpot().getId()
                    + " | въезд: " + session.getEntryTime().format(FORMATTER)
                    + " | тариф: " + getTariffDisplayName(session.getTariffStrategy().getType()));
        }
    }

    private void showReport() {
        System.out.println(occupancyReportService.buildReport());
    }

    private SpotType readSpotType() {
        SpotType[] values = SpotType.values();
        System.out.println("Типы машиномест:");
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i].getDisplayName());
        }
        System.out.print("Выберите тип машиноместа: ");
        int choice = InputUtils.parsePositiveInt(scanner.nextLine(), "Тип машиноместа");
        if (choice > values.length) {
            throw new ParkingException("Некорректный тип машиноместа.");
        }
        return values[choice - 1];
    }

    private VehicleType readVehicleType() {
        VehicleType[] values = VehicleType.values();
        System.out.println("Типы автомобилей:");
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i].getDisplayName());
        }
        System.out.print("Выберите тип автомобиля: ");
        int choice = InputUtils.parsePositiveInt(scanner.nextLine(), "Тип автомобиля");
        if (choice > values.length) {
            throw new ParkingException("Некорректный тип автомобиля.");
        }
        return values[choice - 1];
    }

    private TariffStrategy readTariffStrategy() {
        TariffStrategy[] strategies = parkingService.getAvailableTariffs().toArray(new TariffStrategy[0]);
        System.out.println("Доступные тарифы:");
        for (int i = 0; i < strategies.length; i++) {
            System.out.println((i + 1) + ". " + getTariffDisplayName(strategies[i].getType()) + " (" + strategies[i].getType().name() + ")");
        }
        System.out.print("Выберите тариф: ");
        int choice = InputUtils.parsePositiveInt(scanner.nextLine(), "Тариф");
        if (choice > strategies.length) {
            throw new ParkingException("Некорректный тариф.");
        }
        return strategies[choice - 1];
    }
}
