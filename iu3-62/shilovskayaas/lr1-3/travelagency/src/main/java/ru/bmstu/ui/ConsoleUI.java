package ru.bmstu.ui;

import ru.bmstu.exception.*;
import ru.bmstu.model.*;
import ru.bmstu.repository.ClientRepository;
import ru.bmstu.service.*;
import ru.bmstu.service.interfaces.*;
import ru.bmstu.observer.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
//    private final AdminService adminService;
//    private final BookingService bookingService;
//    private final ClientAuthService clientAuthService;
//    private final Scanner scanner;
//    private final PaymentService paymentService;

    private final IAdminService adminService;
    private final IBookingService bookingService;
    private final IClientAuthService clientAuthService;
    private final IPaymentService paymentService;
    private final Scanner scanner;

    public ConsoleUI(IAdminService adminService, IBookingService bookingService,
                     IClientAuthService clientAuthService, IPaymentService paymentService) {
        this.adminService = adminService;
        this.bookingService = bookingService;
        this.clientAuthService = clientAuthService;
        this.paymentService = paymentService;
        this.scanner = new Scanner(System.in);
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом");
            }
        }
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    private BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число");
            }
        }
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void start() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    adminMode();
                    break;
                case "2":
                    clientLoginOrRegister();
                    break;
                case "0":
                    System.out.println("Конец работы");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\nТУРАГЕНТСТВО");
        System.out.println("1. Войти как администратор");
        System.out.println("2. Войти как клиент");
        System.out.println("0. Выход");
        System.out.print("Выберите: ");
    }

    private void adminMode() {
        while (true) {
            printAdminMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTour();
                    break;
                case "2":
                    removeTour();
                    break;
                case "3":
                    setTourSale();
                    break;
                case "4":
                    viewAllClients();
                    break;
                case "5":
                    setPersonalDiscount();
                    break;
                case "6":
                    viewAllTours();
                    break;
                case "7":
                    viewAllBookings();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private void printAdminMenu() {
        System.out.println("\nАДМИНИСТРАТОР");
        System.out.println("1. Добавить тур");
        System.out.println("2. Удалить тур");
        System.out.println("3. Установить акцию на тур");
        System.out.println("4. Показать всех клиентов");
        System.out.println("5. Установить персональную скидку клиенту");
        System.out.println("6. Показать все туры");
        System.out.println("7. Показать все бронирования");
        System.out.println("0. Назад");
        System.out.print("Выберите: ");
    }

    private void addTour() {
        System.out.println("Выберите тип тура:");
        System.out.println("1. Тур по России");
        System.out.println("2. Тур за границу");
        System.out.println("0. Отмена");
        System.out.print("Выберите: ");

        String typeChoice = scanner.nextLine();

        switch (typeChoice) {
            case "1":
                addDomesticTour();
                break;
            case "2":
                addForeignTour();
                break;
            case "0":
                return;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private void addDomesticTour() {
        try {
            String name = readString("Введите название тура: ");
            String description = readString("Введите описание тура: ");
            BigDecimal price = readBigDecimal("Введите базовую цену (руб): ");
            String region = readString("Введите регион: ");
            String transport = readString("Введите вид транспорта (поезд/автобус/самолет): ");

            Tour tour = adminService.addDomesticTour(name, description, price, region, transport);
            System.out.println("Тур по России успешно добавлен ID: " + tour.getId());

        } catch (Exception e) {
            System.out.println("Ошибка при добавлении тура: " + e.getMessage());
        }
    }

    private void addForeignTour() {
        try {
            String name = readString("Введите название тура: ");
            String description = readString("Введите описание тура: ");
            BigDecimal price = readBigDecimal("Введите базовую цену (руб): ");
            String country = readString("Введите страну: ");

            System.out.print("Требуется ли виза? (да/нет): ");
            String visaAnswer = scanner.nextLine();
            boolean needVisa = visaAnswer.equalsIgnoreCase("да");

            String currency = readString("Введите валюту страны (EUR, USD): ");

            Tour tour = adminService.addForeignTour(name, description, price, country, needVisa, currency);
            System.out.println("Тур за границу успешно добавлен ID: " + tour.getId());

        } catch (Exception e) {
            System.out.println("Ошибка при добавлении тура: " + e.getMessage());
        }
    }

    private void removeTour() {
        try {
            viewAllTours();
            int tourId = readInt("Введите ID тура для удаления: ");

            adminService.removeTour(tourId);
            System.out.println("Тур успешно удален");

        } catch (TourAgencyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void setTourSale() {
        try {
            viewAllTours();
            int tourId = readInt("Введите ID тура: ");
            BigDecimal discount = readBigDecimal("Введите процент скидки: ");

            adminService.setTourSale(tourId, discount);
            System.out.println("Акция успешно " + (discount.compareTo(BigDecimal.ZERO) > 0 ? "установлена" : "отключена"));

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void setPersonalDiscount() {
        try {
            viewAllClients();
            int clientId = readInt("Введите ID клиента: ");
            BigDecimal discount = readBigDecimal("Введите процент персональной скидки: ");

            adminService.setPersonalDiscount(clientId, discount);
            System.out.println("Персональная скидка установлена");

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void viewAllTours() {
        List<Tour> tours = adminService.getAllTours();
        System.out.println("\nСПИСОК ТУРОВ");
        if (tours.isEmpty()) {
            System.out.println("Туров пока нет.");
        } else {
            for (Tour tour : tours) {
                System.out.println(tour);
            }
        }
    }

    private void viewAllClients() {
        List<Client> clients = adminService.getAllClients();
        System.out.println("\nСПИСОК КЛИЕНТОВ");
        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
        } else {
            for (Client client : clients) {
                System.out.println(client);
            }
        }
    }

    private void viewAllBookings() {
        List<Booking> bookings = adminService.getAllBookings();
        System.out.println("\nВСЕ БРОНИРОВАНИЯ");
        if (bookings.isEmpty()) {
            System.out.println("Бронирований пока нет.");
        } else {
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }

    private void clientLoginOrRegister() {
        while (true) {
            System.out.println("\nВХОД / РЕГИСТРАЦИЯ КЛИЕНТА");
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.println("0. Назад");
            System.out.print("Выберите: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loginClient();
                    return;
                case "2":
                    registerClient();
                    return;
                case "0":
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private void registerClient() {
        try {
            System.out.println("\nРЕГИСТРАЦИЯ");
            String name = readString("Введите имя: ");

            System.out.println("Требование: " + ru.bmstu.util.EmailValidator.getEmailRequirements());
            String email = readString("Введите email: ");

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            System.out.print("Подтвердите пароль: ");
            String confirmPassword = scanner.nextLine();

            if (!password.equals(confirmPassword)) {
                System.out.println("Ошибка: пароли не совпадают");
                return;
            }

            Client client = clientAuthService.register(name, email, password);
            System.out.println("Добро пожаловать, " + client.getName() + "!");

            clientMode(client);

        } catch (InvalidEmailException e) {
            System.out.println("Ошибка формата email: " + e.getMessage());
        } catch (TourAgencyException e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
        }
    }

    private void loginClient() {
        System.out.println("\nВХОД");
        String email = readString("Введите email: ");

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        Optional<Client> clientOpt = clientAuthService.login(email, password);

        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Добро пожаловать, " + client.getName() + "!");
            clientMode(client);
        } else {
            System.out.println("Неверный email или пароль.");
        }
    }

    private void clientMode(Client client) {
        System.out.println("\nКЛИЕНТ");
        System.out.println("Клиент: " + client.getName() + " (" + client.getEmail() + ")");
        System.out.println("Ваша персональная скидка: " + client.getPersonalDiscount() + "%");

        while (true) {
            printClientMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    browseTours();
                    break;
                case "2":
                    bookTour(client.getId());
                    break;
                case "3":
                    viewMyBookings(client.getId());
                    break;
                case "4":
                    payForBooking(client.getId());
                    break;
                case "5":
                    cancelMyBooking(client.getId());
                    break;
                case "6":
                    showPriceCalculator(client.getId());
                    break;
                case "0":
                    System.out.println("Выход из режима клиента.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private void printClientMenu() {
        System.out.println("\nМЕНЮ КЛИЕНТА");
        System.out.println("1. Посмотреть все туры");
        System.out.println("2. Забронировать тур");
        System.out.println("3. Мои бронирования");
        System.out.println("4. Оплатить бронирование");
        System.out.println("5. Отменить бронирование");
        System.out.println("6. Калькулятор стоимости тура");
        System.out.println("0. Выйти");
        System.out.print("Выберите: ");
    }

    private void browseTours() {
        List<Tour> tours = adminService.getAllTours();
        System.out.println("\nДОСТУПНЫЕ ТУРЫ");
        if (tours.isEmpty()) {
            System.out.println("Туров пока нет.");
        } else {
            for (Tour tour : tours) {
                System.out.println(tour);
            }
        }
    }

    private void bookTour(int clientId) {
        try {
            browseTours();
            int tourId = readInt("Введите ID тура для бронирования: ");

            System.out.println("\nВЫБОР СКИДКИ");
            System.out.println("Доступные: " + ru.bmstu.service.discount.DiscountStrategyFactory.getAvailableStrategies());
            System.out.println("акция - скидка по акции тура");
            System.out.println("персональная - ваша персональная скидка");
            System.out.println("нет - без скидки");
            System.out.print("Выберите: ");
            String strategyName = scanner.nextLine();


            Booking booking = bookingService.bookTour(clientId, tourId, strategyName);
            System.out.println("Тур успешно забронирован");
            System.out.println("Итоговая цена: " + booking.getFinalPrice() + " руб.");
            System.out.println("ID бронирования: " + booking.getId());

        } catch (TourAgencyException e) {
            System.out.println("Ошибка бронирования: " + e.getMessage());
        }
    }

    private void viewMyBookings(int clientId) {
        List<Booking> bookings = bookingService.getClientBookings(clientId);
        System.out.println("\nМОИ БРОНИРОВАНИЯ");
        if (bookings.isEmpty()) {
            System.out.println("У вас пока нет бронирований.");
        } else {
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }

    private void cancelMyBooking(int clientId) {
        try {
            viewMyBookings(clientId);
            int bookingId = readInt("Введите ID бронирования для отмены: ");

            List<Booking> clientBookings = bookingService.getClientBookings(clientId);
//            boolean belongsToClient = clientBookings.stream()
//                    .anyMatch(b -> b.getId() == bookingId);

            Booking bookingToCancel = clientBookings.stream()
                    .filter(b -> b.getId() == bookingId)
                    .findFirst()
                    .orElse(null);

            if (bookingToCancel == null) {
                System.out.println("Ошибка: это бронирование не найдено");
                return;
            }

            if (!bookingToCancel.canBeCancelled()) {
                System.out.println("Ошибка: бронирование в статусе '" + bookingToCancel.getStatusName() + "' нельзя отменить");
                return;
            }

            bookingService.cancelBooking(bookingId);
            System.out.println("Бронирование успешно отменено");

        } catch (TourAgencyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void payForBooking(int clientId) {
        try {
            List<Booking> bookings = bookingService.getClientBookings(clientId);
            List<Booking> unpaidBookings = new ArrayList<>();
            for (Booking booking : bookings) {
                if (!booking.isPaid()) {
                    unpaidBookings.add(booking);
                }
            }

            if (unpaidBookings.isEmpty()) {
                System.out.println("У вас нет неоплаченных бронирований.");
                return;
            }

            System.out.println("\nНЕОПЛАЧЕННЫЕ БРОНИРОВАНИЯ");
            for (Booking booking : unpaidBookings) {
                System.out.println(booking);
            }

            int bookingId = readInt("Введите ID бронирования для оплаты: ");

            boolean belongsToClient = unpaidBookings.stream()
                    .anyMatch(b -> b.getId() == bookingId);

            if (!belongsToClient) {
                System.out.println("Ошибка: бронирование не найдено или уже оплачено");
                return;
            }

            System.out.println("\nВЫБОР СПОСОБА ОПЛАТЫ");
            System.out.println("1. Оплатить онлайн (банковской картой)");
            System.out.println("2. Оплатить в офисе (карта или наличные)");
            System.out.println("0. Отмена");
            System.out.print("Выберите способ оплаты: ");

            String methodChoice = scanner.nextLine();

            switch (methodChoice) {
                case "1":
                    payOnline(bookingId);
                    break;
                case "2":
                    payOffline(bookingId);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }

        } catch (TourAgencyException e) {
            System.out.println("Ошибка оплаты: " + e.getMessage());
        }
    }

    private void payOnline(int bookingId) {
        System.out.println("\nОПЛАТА ОНЛАЙН");
        String cardNumber = readString("Введите номер карты (16 цифр): ");
        String expiryDate = readString("Введите срок действия (MM/YY): ");
        String cvv = readString("Введите CVV код: ");

        paymentService.payOnline(bookingId, cardNumber, expiryDate, cvv);
    }

    private void payOffline(int bookingId) {
        System.out.println("\nОПЛАТА В ОФИСЕ");
        System.out.println("1. Оплатить банковской картой");
        System.out.println("2. Оплатить наличными");
        System.out.print("Выберите способ: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            paymentService.payOffline(bookingId, "card");
        } else if (choice.equals("2")) {
            paymentService.payOffline(bookingId, "cash");
        } else {
            System.out.println("Неверный выбор");
        }
    }

    private void showPriceCalculator(int clientId) {
        System.out.println("\nКалькулятор стоимости тура");

        browseTours();
        int tourId = readInt("Введите ID тура для расчета: ");

        Tour tour = adminService.getAllTours().stream()
                .filter(t -> t.getId() == tourId)
                .findFirst()
                .orElse(null);

        if (tour == null) {
            System.out.println("Тур не найден");
            return;
        }

        int persons = readInt("Введите количество человек: ");
        int month = readInt("Введите месяц поездки (1-12): ");

        ru.bmstu.antipattern.TourPriceCalculator calculator = new ru.bmstu.antipattern.TourPriceCalculator();

        BigDecimal price = calculator.calculateApproximatePrice(tour, persons, month);

        System.out.println("\nРезультат:");
        System.out.println("Тур: " + tour.getName());
        System.out.println("Количество человек: " + persons);
        System.out.println("Месяц поездки: " + month);
        System.out.println("Стоимость: " + price + " руб.");
        System.out.println("Разница: " + price.subtract(tour.getBasePrice()) + " руб.");
    }
}