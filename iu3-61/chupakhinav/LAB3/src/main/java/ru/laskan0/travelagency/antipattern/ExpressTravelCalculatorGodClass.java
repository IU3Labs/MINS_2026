package ru.laskan0.travelagency.antipattern;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.model.Tour;
import ru.laskan0.travelagency.service.ClientService;
import ru.laskan0.travelagency.service.TourService;

/**
 * Намеренно плохая реализация новой функции для ЛР3.
 * Это God Class: UI, поиск данных, валидация, бизнес-логика,
 * форматирование ответа и журнал пояснений собраны в одном месте.
 */
public class ExpressTravelCalculatorGodClass {
    private final ClientService clientService;
    private final TourService tourService;
    private final Scanner scanner;

    public ExpressTravelCalculatorGodClass(
            ClientService clientService,
            TourService tourService,
            Scanner scanner) {
        this.clientService = clientService;
        this.tourService = tourService;
        this.scanner = scanner;
    }

    public void launch() {
        System.out.println("--- Экспресс-калькулятор стоимости тура ---");
        System.out.println("Результат не сохраняется в систему и не создает бронирование.");
        System.out.println("Этот модуль реализован специально как антипаттерн God Class для ЛР3.");
        System.out.println();

        if (tourService.getAllTours().isEmpty()) {
            System.out.println("В системе нет туров для расчета.");
            return;
        }

        printTours();
        printClients();

        System.out.print("Введите ID тура: ");
        String tourId = scanner.nextLine().trim();
        Tour selectedTour = findTourManually(tourId);
        if (selectedTour == null) {
            System.out.println("Тур не найден. Экспресс-расчет отменен.");
            return;
        }

        System.out.print("Введите ID клиента или Enter для анонимного расчета: ");
        String clientId = scanner.nextLine().trim();
        Client selectedClient = null;
        if (!clientId.isBlank()) {
            selectedClient = findClientManually(clientId);
            if (selectedClient == null) {
                System.out.println("Клиент не найден. Будет выполнен анонимный расчет без клиентской скидки.");
            }
        }

        int travelers = readPositiveInt("Количество туристов: ");
        int nights = readPositiveInt("Количество ночей: ");
        int hotelStars = readChoice("Класс отеля (3/4/5): ", List.of(3, 4, 5));
        int mealType = readChoice("Питание (1 - BB, 2 - HB, 3 - AI): ", List.of(1, 2, 3));
        int transportType = readChoice("Транспорт (1 - автобус, 2 - авиаперелет, 3 - премиум авиаперелет): ",
                List.of(1, 2, 3));
        boolean insurance = readYesNo("Добавить страховку? (y/n): ");
        boolean urgentVisa = readYesNo("Нужна срочная виза? (y/n): ");
        boolean fridayPromo = readYesNo("Применить пятничную акцию менеджера? (y/n): ");
        boolean airportTransfer = readYesNo("Добавить трансфер из аэропорта? (y/n): ");
        boolean seaView = readYesNo("Нужен номер с видом на море? (y/n): ");

        double base = selectedTour.getBasePrice() * travelers;
        List<String> explanation = new ArrayList<>();
        explanation.add("База = стоимость тура " + money(selectedTour.getBasePrice()) + " x " + travelers + " чел.");

        double hotelMultiplier = 1.0;
        if (hotelStars == 3) {
            hotelMultiplier = 1.00;
            explanation.add("3* отель: базовый коэффициент 1.00");
        } else if (hotelStars == 4) {
            hotelMultiplier = 1.18;
            explanation.add("4* отель: коэффициент 1.18");
        } else if (hotelStars == 5) {
            hotelMultiplier = 1.37;
            explanation.add("5* отель: коэффициент 1.37");
        }

        double nightsSurcharge = 0;
        if (nights > 7) {
            nightsSurcharge = (nights - 7) * 3200 * travelers;
            explanation.add("Доплата за ночи сверх пакета: " + money(nightsSurcharge));
        } else if (nights < 7) {
            nightsSurcharge = -(7 - nights) * 1700 * travelers;
            explanation.add("Условная скидка за короткий пакет: " + money(nightsSurcharge));
        } else {
            explanation.add("Стандартный пакет 7 ночей: без корректировки по длительности");
        }

        double mealSurcharge = 0;
        if (mealType == 1) {
            mealSurcharge = 0;
            explanation.add("Питание BB: без доплаты");
        }
        if (mealType == 2) {
            mealSurcharge = 2800 * nights * travelers;
            explanation.add("Питание HB: доплата " + money(mealSurcharge));
        }
        if (mealType == 3) {
            mealSurcharge = 5100 * nights * travelers;
            explanation.add("Питание AI: доплата " + money(mealSurcharge));
        }

        double transportSurcharge = 0;
        if (transportType == 1) {
            transportSurcharge = 4500 * travelers;
            explanation.add("Автобус: " + money(transportSurcharge));
        }
        if (transportType == 2) {
            transportSurcharge = 14500 * travelers;
            explanation.add("Авиаперелет: " + money(transportSurcharge));
        }
        if (transportType == 3) {
            transportSurcharge = 24500 * travelers;
            explanation.add("Премиум авиаперелет: " + money(transportSurcharge));
        }

        double insuranceSurcharge = 0;
        if (insurance) {
            insuranceSurcharge = travelers * nights * 420;
            explanation.add("Страховка: " + money(insuranceSurcharge));
        } else {
            explanation.add("Страховка не добавлялась");
        }

        double visaSurcharge = 0;
        if (urgentVisa) {
            visaSurcharge = 8500 * travelers;
            explanation.add("Срочная виза: " + money(visaSurcharge));
        } else {
            explanation.add("Срочная виза не требуется");
        }

        double transferSurcharge = 0;
        if (airportTransfer) {
            transferSurcharge = 1900 * travelers;
            explanation.add("Трансфер: " + money(transferSurcharge));
        } else {
            explanation.add("Трансфер не добавлялся");
        }

        double seaViewSurcharge = 0;
        if (seaView) {
            seaViewSurcharge = 3600 * nights;
            explanation.add("Вид на море: " + money(seaViewSurcharge));
        } else {
            explanation.add("Обычный номер без вида на море");
        }

        double seasonalFactor = 1.0;
        int month = LocalDate.now().getMonthValue();
        if (month == 6 || month == 7 || month == 8) {
            seasonalFactor = 1.22;
            explanation.add("Высокий сезон: коэффициент 1.22");
        } else if (month == 12 || month == 1) {
            seasonalFactor = 1.15;
            explanation.add("Праздничный сезон: коэффициент 1.15");
        } else if (month == 2 || month == 11) {
            seasonalFactor = 0.93;
            explanation.add("Низкий сезон: коэффициент 0.93");
        } else {
            explanation.add("Обычный сезон: коэффициент 1.00");
        }

        double discountAmount = 0;
        if (selectedClient != null && selectedClient.getDiscount() != null) {
            Discount discount = selectedClient.getDiscount();
            discountAmount = ((base * hotelMultiplier) + nightsSurcharge) * discount.getPercent() / 100.0;
            explanation.add("Клиентская скидка " + discount.getName() + " (" + discount.getPercent() + "%) = -"
                    + money(discountAmount));
        } else {
            explanation.add("Клиентская скидка не применялась");
        }

        double fridayDiscount = 0;
        if (fridayPromo && LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY) {
            fridayDiscount = (base * hotelMultiplier) * 0.15;
            explanation.add("Пятничная акция менеджера = -" + money(fridayDiscount));
        } else if (fridayPromo) {
            fridayDiscount = (base * hotelMultiplier) * 0.05;
            explanation.add("Акция запрошена не в пятницу, поэтому применен ручной компромисс 5% = -"
                    + money(fridayDiscount));
        } else {
            explanation.add("Акция не применялась");
        }

        double serviceFee = 2900 + travelers * 650;
        explanation.add("Сервисный сбор = " + money(serviceFee));

        double subtotal = (base * hotelMultiplier)
                + nightsSurcharge
                + mealSurcharge
                + transportSurcharge
                + insuranceSurcharge
                + visaSurcharge
                + transferSurcharge
                + seaViewSurcharge
                + serviceFee
                - discountAmount
                - fridayDiscount;

        double total = subtotal * seasonalFactor;
        double prepayment = total * 0.3;
        double monthlyPayment = (total - prepayment) / 3.0;

        System.out.println();
        System.out.println("=== Экспресс-предложение ===");
        System.out.println("Тур: " + selectedTour.getTitle() + " (" + selectedTour.getDestination() + ")");
        System.out.println("Клиент: " + (selectedClient == null ? "анонимный расчет" : selectedClient.getFullName()));
        System.out.println("Путешественники: " + travelers + ", ночей: " + nights + ", отель: " + hotelStars + "*");
        System.out.println("Итоговая примерная стоимость: " + money(total));
        System.out.println("Предоплата 30%: " + money(prepayment));
        System.out.println("Остаток тремя платежами: по " + money(monthlyPayment));
        System.out.println();
        System.out.println("Детализация:");
        for (String line : explanation) {
            System.out.println("- " + line);
        }
        System.out.println();
        System.out.println("ВНИМАНИЕ: это приблизительный расчет. Он не сохраняется, не резервирует места и может расходиться");
        System.out.println("с итоговой ценой реального бронирования, потому что модуль реализован отдельно и намеренно плохо спроектирован.");
    }

    private void printTours() {
        System.out.println("Доступные туры:");
        for (Tour tour : tourService.getAllTours()) {
            System.out.println("  " + tour.getId() + " -> " + tour.getTitle() + " / " + tour.getDestination()
                    + " / базовая цена " + money(tour.getBasePrice()));
        }
        System.out.println();
    }

    private void printClients() {
        System.out.println("Клиенты:");
        for (Client client : clientService.getAllClients()) {
            String discount = client.getDiscount() == null
                    ? "без скидки"
                    : client.getDiscount().getName() + " " + client.getDiscount().getPercent() + "%";
            System.out.println("  " + client.getId() + " -> " + client.getFullName() + " / " + discount);
        }
        System.out.println();
    }

    private Tour findTourManually(String tourId) {
        for (Tour tour : tourService.getAllTours()) {
            if (tour.getId().equalsIgnoreCase(tourId)) {
                return tour;
            }
        }
        return null;
    }

    private Client findClientManually(String clientId) {
        for (Client client : clientService.getAllClients()) {
            if (client.getId().equalsIgnoreCase(clientId)) {
                return client;
            }
        }
        return null;
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Намеренно не разделяем ответственность и формат ошибок внутри God Class.
            }
            System.out.println("Введите положительное целое число.");
        }
    }

    private int readChoice(String prompt, List<Integer> allowedValues) {
        while (true) {
            int value = readPositiveInt(prompt);
            if (allowedValues.contains(value)) {
                return value;
            }
            System.out.println("Недопустимое значение. Разрешено: " + allowedValues);
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (value.equals("y") || value.equals("yes") || value.equals("д") || value.equals("да")) {
                return true;
            }
            if (value.equals("n") || value.equals("no") || value.equals("н") || value.equals("нет")) {
                return false;
            }
            System.out.println("Введите y/n.");
        }
    }

    private String money(double value) {
        return String.format(Locale.US, "%.2f руб.", value);
    }
}
