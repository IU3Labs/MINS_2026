import promo.QuickPromoCalculator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import command.CommandManager;
import command.EnterVehicleCommand;
import command.ExitVehicleCommand;
import core.ParkingSystem;
import exception.ParkingException;
import model.ParkingTicket;
import observer.CapacityAlertObserver;
import observer.ConsoleLogger;
import report.TextReportGenerator;
import storage.FileParkingStorage;
import tariff.HourlyTariffCalculator;
import tariff.decorator.DiscountDecorator;
import tariff.decorator.NightSurchargeDecorator;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static ParkingSystem system;
    private static CommandManager commandManager;
    public static void main(String[] args) {
        system = new ParkingSystem(
                5,
                new HourlyTariffCalculator(300.0),
                new TextReportGenerator(),
                new FileParkingStorage("parking_data.txt")
        );

        commandManager = new CommandManager();

        system.subscribe(new ConsoleLogger());
        system.subscribe(new CapacityAlertObserver(80));

        System.out.println("=== ИНФОРМАЦИОННАЯ СИСТЕМА ПАРКОВКИ ===");
        boolean run = true;

        while (run) {
            System.out.println("\n1. Въезд");
            System.out.println("2. Выезд");
            System.out.println("3. Отчёт по загрузке");
            System.out.println("4. Управление уведомлениями");
            System.out.println("5. Журнал операций");
            System.out.println("6. Применить ночную наценку (+50%) ко всем");
            System.out.println("7. Применить скидку (-20%) к конкретному месту");
            System.out.println("8. Сбросить все тарифы к базовым");
            System.out.println("9. Быстрый калькулятор акции");
            System.out.println("10. Выход");
            System.out.print("Выбор: ");
            String cmd = sc.nextLine().trim();

            try {
                switch (cmd) {
                    case "1" -> handleEnter();
                    case "2" -> handleExit();
                    case "3" -> {
                        System.out.println(system.getReport());
                        System.out.printf("Выручка за всё время: %.2f руб.\n", system.getTotalRevenue());
                    }
                    case "4" -> handleObservers();
                    case "5" -> commandManager.printLog();
                    case "6" -> {
                        system.setGlobalTariff(new NightSurchargeDecorator(system.getGlobalTariff(), 1.5));
                        System.out.println("Ночная наценка 50% применена глобально.");
                    }
                    case "7" -> {
                        System.out.print("Введите номер места для скидки: ");
                        int space = Integer.parseInt(sc.nextLine().trim());
                        system.setSpaceTariff(space, new DiscountDecorator(system.getGlobalTariff(), 0.8));
                        System.out.println("Скидка 20% применена к месту #" + space);
                    }
                    case "8" -> {
                        system.setGlobalTariff(new HourlyTariffCalculator(150.0));
                        System.out.println("Все тарифы сброшены к базовым.");
                    }
                    case "9" -> new QuickPromoCalculator().calculateAndShow();
                    case "10" -> run = false;
                    default -> System.out.println("Неверная команда.");
                }
            } catch (ParkingException e) {
                System.err.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ошибка ввода данных: " + e.getMessage());
            }
        }
        System.out.println("Работа завершена.");
    }

    private static LocalDateTime readTime() {
        System.out.print("Время (ГГГГ-ММ-ДД ЧЧ:ММ) или Enter для сейчас: ");
        String input = sc.nextLine().trim();
        return input.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(input, FMT);
    }

    private static void handleEnter() {
        System.out.print("Гос. номер: ");
        String plate = sc.nextLine().trim();
        EnterVehicleCommand command = new EnterVehicleCommand(system, plate, readTime());
        if (commandManager.execute(command)) {
            ParkingTicket t = command.getResultTicket();
            System.out.println("Въезд разрешён. Место #" + t.getSpaceId());
        }
    }

    private static void handleExit() {
        System.out.print("Номер места: ");
        int space = Integer.parseInt(sc.nextLine().trim());
        ExitVehicleCommand command = new ExitVehicleCommand(system, space, readTime());
        if (commandManager.execute(command)) {
            System.out.printf("Выезд разрешён. Сумма: %.2f руб.\n", command.getCost());
        }
    }

    private static void handleObservers() {
        System.out.println("Активных наблюдателей: " + system.getObserverCount());
        System.out.println("1. Подписаться на логи (ConsoleLogger)");
        System.out.println("2. Отписаться от наблюдателей типа ConsoleLogger");
        System.out.print("Действие: ");
        String sub = sc.nextLine().trim();
        switch (sub) {
            case "1" -> {
                system.subscribe(new ConsoleLogger());
                System.out.println("Подписка добавлена.");
            }
            case "2" -> {
                system.removeObserverByType(ConsoleLogger.class);
                System.out.println("Наблюдатели типа ConsoleLogger удалены.");
            }
            default -> System.out.println("Отмена.");
        }
    }
}