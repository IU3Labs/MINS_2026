package ru.bmstu.travel.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import ru.bmstu.travel.common.Tracing;
import ru.bmstu.travel.proto.core.AddClientRequest;
import ru.bmstu.travel.proto.core.AddTourRequest;
import ru.bmstu.travel.proto.core.CancelBookingRequest;
import ru.bmstu.travel.proto.core.CoreServiceGrpc;
import ru.bmstu.travel.proto.core.CreateBookingRequest;
import ru.bmstu.travel.proto.core.Discount;
import ru.bmstu.travel.proto.core.DiscountType;
import ru.bmstu.travel.proto.core.Empty;
import ru.bmstu.travel.proto.core.QuickEstimateRequest;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleClientApp {
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^C-\\d{3}$");
    private static final Pattern TOUR_ID_PATTERN = Pattern.compile("^T-\\d{3}$");
    private static final Pattern BOOKING_ID_PATTERN = Pattern.compile("^B-\\d{3}$");

    private final ManagedChannel channel;
    private final Scanner scanner;

    public ConsoleClientApp(String target) {
        this.channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ConsoleClientApp client = new ConsoleClientApp("127.0.0.1:50051");
        try {
            client.run();
        } finally {
            client.shutdown();
        }
    }

    public void shutdown() {
        channel.shutdownNow();
    }

    public void run() {
        while (true) {
            printMenu();
            String choice = readLine("Выберите пункт меню: ");
            switch (choice) {
                case "1" -> showTours();
                case "2" -> showClients();
                case "3" -> showBookings();
                case "4" -> addTour();
                case "5" -> addClient();
                case "6" -> createBooking();
                case "7" -> cancelBooking();
                case "8" -> showHistory();
                case "9" -> quickEstimate();
                case "0" -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Такого пункта меню нет.");
            }
            System.out.println();
        }
    }

    private CoreServiceGrpc.CoreServiceBlockingStub stub() {
        return MetadataUtils.attachHeaders(
                CoreServiceGrpc.newBlockingStub(channel),
                Tracing.metadataWithTraceId(Tracing.newTraceId())
        );
    }

    private void printStatusError(Status.Code code, String details) {
        System.out.printf("%s: %s%n", code, details);
    }

    private boolean validateId(String value, Pattern pattern, String entityName, String example) {
        if (pattern.matcher(value).matches()) {
            return true;
        }
        printStatusError(Status.Code.INVALID_ARGUMENT, String.format("Некорректный ID %s. Ожидаемый формат: %s.", entityName, example));
        return false;
    }

    private void printMenu() {
        System.out.println("===== ТУРАГЕНТСТВО / ЛР4 =====");
        System.out.println("1. Показать туры");
        System.out.println("2. Показать клиентов");
        System.out.println("3. Показать бронирования");
        System.out.println("4. Добавить тур");
        System.out.println("5. Добавить клиента");
        System.out.println("6. Создать бронирование");
        System.out.println("7. Отменить бронирование");
        System.out.println("8. Показать журнал изменений");
        System.out.println("9. Экспресс-расчет стоимости тура (God Class)");
        System.out.println("0. Выход");
    }

    private void showTours() {
        safeRun(() -> {
            var response = stub().listTours(Empty.newBuilder().build());
            if (!response.getSuccess()) {
                System.out.println(response.getMessage());
                return;
            }
            System.out.println("--- Туры ---");
            for (var tour : response.getToursList()) {
                System.out.printf("%s: %s / %s / цена %.2f / свободно %d%n",
                        tour.getId(),
                        tour.getTitle(),
                        tour.getDestination(),
                        tour.getBasePrice(),
                        tour.getCapacity() - tour.getBookedSeats());
            }
        });
    }

    private void showClients() {
        safeRun(() -> {
            var response = stub().listClients(Empty.newBuilder().build());
            if (!response.getSuccess()) {
                System.out.println(response.getMessage());
                return;
            }
            System.out.println("--- Клиенты ---");
            for (var client : response.getClientsList()) {
                String discount = "без скидки";
                if (client.getHasDiscount()) {
                    discount = String.format("%s (%d%%)", client.getDiscount().getName(), client.getDiscount().getPercent());
                }
                System.out.printf("%s: %s / %s / %s%n", client.getId(), client.getFullName(), client.getPhone(), discount);
            }
        });
    }

    private void showBookings() {
        safeRun(() -> {
            var response = stub().listBookings(Empty.newBuilder().build());
            System.out.println("--- Бронирования ---");
            if (response.getBookingsList().isEmpty()) {
                System.out.println("Список бронирований пуст.");
                return;
            }
            for (var booking : response.getBookingsList()) {
                System.out.printf("%s: %s -> %s / %.2f / %s%n",
                        booking.getId(), booking.getClientName(), booking.getTourTitle(), booking.getFinalPrice(), booking.getStatus());
            }
        });
    }

    private void addTour() {
        String title = readLine("Название тура: ");
        String destination = readLine("Направление: ");
        double basePrice = readDouble("Базовая цена: ");
        int capacity = readInt("Количество мест: ");
        safeRun(() -> {
            var response = stub().addTour(AddTourRequest.newBuilder()
                    .setTitle(title)
                    .setDestination(destination)
                    .setBasePrice(basePrice)
                    .setCapacity(capacity)
                    .build());
            System.out.println(response.getMessage());
            if (response.getSuccess()) {
                System.out.printf("Добавлен тур %s.%n", response.getTour().getId());
            }
        });
    }

    private void addClient() {
        String fullName = readLine("ФИО: ");
        String phone = readLine("Телефон: ");
        boolean hasDiscount = readYesNo("Есть скидка? (yes/no): ");
        AddClientRequest.Builder request = AddClientRequest.newBuilder()
                .setFullName(fullName)
                .setPhone(phone)
                .setHasDiscount(hasDiscount);
        if (hasDiscount) {
            String discountName = readLine("Название скидки: ");
            int percent = readInt("Процент скидки: ");
            System.out.println("Тип скидки: 1 - PERCENTAGE, 2 - VIP, 3 - SEASONAL");
            int discountChoice = readInt("Выберите тип скидки: ");
            DiscountType type = switch (discountChoice) {
                case 1 -> DiscountType.PERCENTAGE;
                case 2 -> DiscountType.VIP;
                case 3 -> DiscountType.SEASONAL;
                default -> DiscountType.DISCOUNT_TYPE_UNSPECIFIED;
            };
            request.setDiscount(Discount.newBuilder().setName(discountName).setPercent(percent).setType(type).build());
        }
        safeRun(() -> {
            var response = stub().addClient(request.build());
            System.out.println(response.getMessage());
            if (response.getSuccess()) {
                System.out.printf("Добавлен клиент %s.%n", response.getClient().getId());
            }
        });
    }

    private void createBooking() {
        String clientId = readLine("ID клиента: ");
        if (!validateId(clientId, CLIENT_ID_PATTERN, "клиента", "C-001")) {
            return;
        }
        String tourId = readLine("ID тура: ");
        if (!validateId(tourId, TOUR_ID_PATTERN, "тура", "T-001")) {
            return;
        }
        safeRun(() -> {
            var response = stub().createBooking(CreateBookingRequest.newBuilder().setClientId(clientId).setTourId(tourId).build());
            System.out.println(response.getMessage());
            if (response.getSuccess()) {
                System.out.printf("Создано бронирование %s.%n", response.getBooking().getId());
            }
        });
    }

    private void cancelBooking() {
        String bookingId = readLine("ID бронирования: ");
        if (!validateId(bookingId, BOOKING_ID_PATTERN, "бронирования", "B-001")) {
            return;
        }
        safeRun(() -> {
            var response = stub().cancelBooking(CancelBookingRequest.newBuilder().setBookingId(bookingId).build());
            System.out.println(response.getMessage());
        });
    }

    private void showHistory() {
        safeRun(() -> {
            var response = stub().listHistory(Empty.newBuilder().build());
            System.out.println("--- История ---");
            if (response.getEventsList().isEmpty()) {
                System.out.println("История пуста.");
                return;
            }
            for (String event : response.getEventsList()) {
                System.out.println("- " + event);
            }
        });
    }

    private void quickEstimate() {
        String tourId = readLine("ID тура: ");
        if (!validateId(tourId, TOUR_ID_PATTERN, "тура", "T-001")) {
            return;
        }
        String clientId = readLine("ID клиента (Enter для анонимного расчета): ");
        if (!clientId.isBlank() && !validateId(clientId, CLIENT_ID_PATTERN, "клиента", "C-001")) {
            return;
        }
        int travelers = readInt("Количество туристов: ");
        int nights = readInt("Количество ночей: ");
        int hotelStars = readInt("Класс отеля (3/4/5): ");
        Map<Integer, String> mealMap = Map.of(1, "BB", 2, "HB", 3, "AI");
        Map<Integer, String> transportMap = Map.of(1, "BUS", 2, "AIR", 3, "PREMIUM_AIR");
        System.out.println("Питание: 1 - BB, 2 - HB, 3 - AI");
        String mealType = mealMap.getOrDefault(readInt("Выберите тип питания: "), "BB");
        System.out.println("Транспорт: 1 - автобус, 2 - авиаперелет, 3 - премиум авиаперелет");
        String transportType = transportMap.getOrDefault(readInt("Выберите транспорт: "), "BUS");

        QuickEstimateRequest request = QuickEstimateRequest.newBuilder()
                .setTourId(tourId)
                .setClientId(clientId)
                .setTravelers(travelers)
                .setNights(nights)
                .setHotelStars(hotelStars)
                .setMealType(mealType)
                .setTransportType(transportType)
                .setInsurance(readYesNo("Добавить страховку? (yes/no): "))
                .setUrgentVisa(readYesNo("Нужна срочная виза? (yes/no): "))
                .setFridayPromo(readYesNo("Применить пятничную акцию? (yes/no): "))
                .setAirportTransfer(readYesNo("Добавить трансфер? (yes/no): "))
                .setSeaView(readYesNo("Номер с видом на море? (yes/no): "))
                .build();

        safeRun(() -> {
            var response = stub().quickEstimate(request);
            if (!response.getSuccess()) {
                System.out.println(response.getMessage());
                return;
            }
            System.out.printf("--- %s ---%n", response.getHeadline());
            System.out.printf("Итоговая стоимость: %.2f руб.%n", response.getTotal());
            System.out.printf("Предоплата 30%%: %.2f руб.%n", response.getPrepayment());
            System.out.printf("Остаток тремя платежами: по %.2f руб.%n", response.getMonthlyPayment());
            System.out.println("Детализация:");
            for (String line : response.getExplanationList()) {
                System.out.println("- " + line);
            }
        });
    }

    private void safeRun(Runnable action) {
        try {
            action.run();
        } catch (StatusRuntimeException exc) {
            String description = exc.getStatus().getDescription();
            if (description == null || description.isBlank()) {
                description = "Описание ошибки отсутствует.";
            }
            printStatusError(exc.getStatus().getCode(), description);
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException exc) {
                System.out.println("Нужно ввести целое число.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt).replace(',', '.'));
            } catch (NumberFormatException exc) {
                System.out.println("Нужно ввести число.");
            }
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String raw = readLine(prompt).toLowerCase();
            if (raw.equals("y") || raw.equals("yes") || raw.equals("д") || raw.equals("да")) {
                return true;
            }
            if (raw.equals("n") || raw.equals("no") || raw.equals("н") || raw.equals("нет")) {
                return false;
            }
            System.out.println("Введите yes/no.");
        }
    }
}
