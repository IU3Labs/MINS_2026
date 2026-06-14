package ru.laskan0.travelagency.ui;

import java.util.List;
import java.util.Scanner;

import ru.laskan0.travelagency.exception.BookingStateException;
import ru.laskan0.travelagency.exception.DuplicateEntityException;
import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.exception.NotFoundException;
import ru.laskan0.travelagency.exception.PricingException;
import ru.laskan0.travelagency.exception.TourAvailabilityException;
import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.model.DiscountType;
import ru.laskan0.travelagency.model.Tour;
import ru.laskan0.travelagency.service.BookingService;
import ru.laskan0.travelagency.service.ClientService;
import ru.laskan0.travelagency.service.TourService;
import ru.laskan0.travelagency.service.observer.TravelAgencyHistoryObserver;
import ru.laskan0.travelagency.util.InputHandler;

public class ConsoleUi {
    private final ClientService clientService;
    private final TourService tourService;
    private final BookingService bookingService;
    private final TravelAgencyHistoryObserver historyObserver;
    private final InputHandler inputHandler;

    public ConsoleUi(
            ClientService clientService,
            TourService tourService,
            BookingService bookingService,
            TravelAgencyHistoryObserver historyObserver,
            Scanner scanner) {
        this.clientService = clientService;
        this.tourService = tourService;
        this.bookingService = bookingService;
        this.historyObserver = historyObserver;
        this.inputHandler = new InputHandler(scanner);
    }

    public void start() {
        seedDemoData();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = inputHandler.readInt("Выберите пункт меню: ");
            try {
                switch (choice) {
                    case 1 -> showTours();
                    case 2 -> showClients();
                    case 3 -> showBookings();
                    case 4 -> addTour();
                    case 5 -> addClient();
                    case 6 -> createBooking();
                    case 7 -> cancelBooking();
                    case 8 -> showHistory();
                    case 0 -> running = false;
                    default -> System.out.println("Неизвестный пункт меню");
                }
            } catch (InvalidDataException exception) {
                System.out.println("Некорректные данные: " + exception.getMessage());
            } catch (DuplicateEntityException exception) {
                System.out.println("Конфликт данных: " + exception.getMessage());
            } catch (NotFoundException exception) {
                System.out.println("Не найдено: " + exception.getMessage());
            } catch (TourAvailabilityException exception) {
                System.out.println("Нет свободных мест: " + exception.getMessage());
            } catch (BookingStateException exception) {
                System.out.println("Недопустимое состояние бронирования: " + exception.getMessage());
            } catch (PricingException exception) {
                System.out.println("Ошибка расчета цены: " + exception.getMessage());
            }
            System.out.println();
        }
        System.out.println("Работа программы завершена.");
    }

    private void printMenu() {
        System.out.println("=== Турагентство ===");
        System.out.println("1. Показать туры");
        System.out.println("2. Показать клиентов");
        System.out.println("3. Показать бронирования");
        System.out.println("4. Добавить тур");
        System.out.println("5. Добавить клиента");
        System.out.println("6. Создать бронирование");
        System.out.println("7. Отменить бронирование");
        System.out.println("8. Показать журнал изменений системы");
        System.out.println("0. Выход");
    }

    private void showTours() {
        printCollection("Список туров", tourService.getAllTours());
    }

    private void showClients() {
        printCollection("Список клиентов", clientService.getAllClients());
    }

    private void showBookings() {
        printCollection("Список бронирований", bookingService.getAllBookings());
    }

    private void showHistory() {
        printCollection("Журнал изменений системы", historyObserver.getEventLog());
    }

    private void printCollection(String title, List<?> items) {
        System.out.println("--- " + title + " ---");
        if (items.isEmpty()) {
            System.out.println("Список пуст");
            return;
        }
        items.forEach(System.out::println);
    }

    private void addTour() {
        String title = inputHandler.readRequiredLine("Название тура: ");
        String destination = inputHandler.readRequiredLine("Направление: ");
        double price = inputHandler.readDouble("Базовая цена: ");
        int capacity = inputHandler.readInt("Количество мест: ");

        Tour tour = tourService.addTour(title, destination, price, capacity);
        System.out.println("Тур добавлен. Назначенный ID: " + tour.getId());
    }

    private void addClient() {
        String fullName = inputHandler.readRequiredLine("ФИО: ");
        String phone = inputHandler.readRequiredLine("Телефон: ");

        Discount discount = null;
        if (inputHandler.readYesNo("Есть скидка?")) {
            String discountName = inputHandler.readRequiredLine("Название скидки: ");
            int percent = inputHandler.readInt("Процент скидки: ");

            System.out.println("Тип скидки:");
            System.out.println("1. PERCENTAGE");
            System.out.println("2. VIP");
            System.out.println("3. SEASONAL");

            int typeChoice = inputHandler.readInt("Выберите тип скидки: ");

            DiscountType discountType = switch (typeChoice) {
                case 1 -> DiscountType.PERCENTAGE;
                case 2 -> DiscountType.VIP;
                case 3 -> DiscountType.SEASONAL;
                default -> throw new InvalidDataException("Неизвестный тип скидки");
            };

            discount = new Discount(discountName, percent, discountType);
        }

        Client client = clientService.addClient(fullName, phone, discount);
        System.out.println("Клиент добавлен. Назначенный ID: " + client.getId());
    }

    private void createBooking() {
        Client client = readExistingClient("ID клиента: ");
        Tour tour = readAvailableTour("ID тура: ");

        Booking booking = bookingService.createBooking(client, tour);
        System.out.println("Бронирование создано. Назначенный ID: " + booking.getId());
    }

    private void cancelBooking() {
        Booking booking = readExistingBooking("ID бронирования: ");
        bookingService.cancelBooking(booking.getId());
        System.out.println("Бронирование отменено.");
    }

    private Client readExistingClient(String prompt) {
        String clientId = inputHandler.readRequiredLine(prompt);
        return clientService.getClientById(clientId);
    }

    private Tour readAvailableTour(String prompt) {
        String tourId = inputHandler.readRequiredLine(prompt);
        Tour tour = tourService.getTourById(tourId);
        if (!tour.hasAvailableSeats()) {
            throw new TourAvailabilityException("В туре больше нет свободных мест: " + tour.getId());
        }
        return tour;
    }

    private Booking readExistingBooking(String prompt) {
        String bookingId = inputHandler.readRequiredLine(prompt);
        return bookingService.getBookingById(bookingId);
    }

    private void seedDemoData() {
        if (!tourService.getAllTours().isEmpty() || !clientService.getAllClients().isEmpty()) {
            return;
        }

        tourService.addTour("Морской отдых", "Турция", 75000, 10);
        tourService.addTour("Европейский уикенд", "Италия", 92000, 6);

        clientService.addClient("Иван Петров", "+7-900-000-00-01",
                new Discount("Постоянный клиент", 10, DiscountType.VIP));
        clientService.addClient("Анна Смирнова", "+7-900-000-00-02", null);
    }
}
