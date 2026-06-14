package ru.bmstu.travel.core.service;

import ru.bmstu.travel.core.catalog.CatalogClient;
import ru.bmstu.travel.core.catalog.CatalogTour;
import ru.bmstu.travel.core.exception.InvalidDataException;
import ru.bmstu.travel.core.exception.NotFoundException;
import ru.bmstu.travel.core.gateway.ReferenceGateway;
import ru.bmstu.travel.proto.core.QuickEstimateRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressTravelCalculatorGodClass {
    private final ReferenceGateway gateway;
    private final Clock clock;

    public ExpressTravelCalculatorGodClass(ReferenceGateway gateway) {
        this(gateway, Clock.systemDefaultZone());
    }

    public ExpressTravelCalculatorGodClass(ReferenceGateway gateway, Clock clock) {
        this.gateway = gateway;
        this.clock = clock;
    }

    public EstimateResult calculate(QuickEstimateRequest request, String traceId) {
        if (request.getTravelers() <= 0) {
            throw new InvalidDataException("Количество туристов должно быть больше 0.");
        }
        if (request.getNights() <= 0) {
            throw new InvalidDataException("Количество ночей должно быть больше 0.");
        }
        if (request.getHotelStars() != 3 && request.getHotelStars() != 4 && request.getHotelStars() != 5) {
            throw new InvalidDataException("Класс отеля должен быть 3, 4 или 5.");
        }
        if (!List.of("BB", "HB", "AI").contains(request.getMealType())) {
            throw new InvalidDataException("Тип питания должен быть BB, HB или AI.");
        }
        if (!List.of("BUS", "AIR", "PREMIUM_AIR").contains(request.getTransportType())) {
            throw new InvalidDataException("Тип транспорта задан некорректно.");
        }

        CatalogTour tour = gateway.getTour(request.getTourId(), traceId);
        if (tour == null) {
            throw new NotFoundException("Тур не найден: " + request.getTourId());
        }

        CatalogClient client = null;
        if (!request.getClientId().isBlank()) {
            client = gateway.getClient(request.getClientId(), traceId);
            if (client == null) {
                throw new NotFoundException("Клиент не найден: " + request.getClientId());
            }
        }

        LocalDate today = LocalDate.now(clock);
        List<String> explanation = new ArrayList<>();
        double base = tour.basePrice() * request.getTravelers();
        explanation.add(String.format("База: %.2f x %d чел. = %.2f руб.", tour.basePrice(), request.getTravelers(), base));

        double hotelMultiplier = Map.of(3, 1.0, 4, 1.18, 5, 1.37).get(request.getHotelStars());
        explanation.add(String.format("Коэффициент отеля %d*: %.2f.", request.getHotelStars(), hotelMultiplier));

        double nightsDelta;
        if (request.getNights() > 7) {
            nightsDelta = (request.getNights() - 7) * 3200.0 * request.getTravelers();
            explanation.add(String.format("Доплата за ночи сверх пакета: %.2f руб.", nightsDelta));
        } else if (request.getNights() < 7) {
            nightsDelta = -(7 - request.getNights()) * 1700.0 * request.getTravelers();
            explanation.add(String.format("Условная скидка за короткий пакет: %.2f руб.", nightsDelta));
        } else {
            nightsDelta = 0.0;
            explanation.add("Стандартный пакет 7 ночей: без корректировки по длительности.");
        }

        double mealPrice = switch (request.getMealType()) {
            case "HB" -> 2800.0 * request.getNights() * request.getTravelers();
            case "AI" -> 5100.0 * request.getNights() * request.getTravelers();
            default -> 0.0;
        };
        double transportPrice = switch (request.getTransportType()) {
            case "BUS" -> 4500.0 * request.getTravelers();
            case "AIR" -> 14500.0 * request.getTravelers();
            case "PREMIUM_AIR" -> 24500.0 * request.getTravelers();
            default -> 0.0;
        };
        explanation.add(String.format("Питание %s: %.2f руб.", request.getMealType(), mealPrice));
        explanation.add(String.format("Транспорт %s: %.2f руб.", request.getTransportType(), transportPrice));

        double insurance = request.getInsurance() ? request.getTravelers() * request.getNights() * 420.0 : 0.0;
        double urgentVisa = request.getUrgentVisa() ? 8500.0 * request.getTravelers() : 0.0;
        double airportTransfer = request.getAirportTransfer() ? 1900.0 * request.getTravelers() : 0.0;
        double seaView = request.getSeaView() ? 3600.0 * request.getNights() : 0.0;
        if (insurance > 0) {
            explanation.add(String.format("Страховка: %.2f руб.", insurance));
        }
        if (urgentVisa > 0) {
            explanation.add(String.format("Срочная виза: %.2f руб.", urgentVisa));
        }
        if (airportTransfer > 0) {
            explanation.add(String.format("Трансфер: %.2f руб.", airportTransfer));
        }
        if (seaView > 0) {
            explanation.add(String.format("Вид на море: %.2f руб.", seaView));
        }

        double seasonalFactor;
        int month = today.getMonthValue();
        if (month == 6 || month == 7 || month == 8) {
            seasonalFactor = 1.22;
            explanation.add("Высокий сезон: коэффициент 1.22.");
        } else if (month == 12 || month == 1) {
            seasonalFactor = 1.15;
            explanation.add("Праздничный сезон: коэффициент 1.15.");
        } else if (month == 2 || month == 11) {
            seasonalFactor = 0.93;
            explanation.add("Низкий сезон: коэффициент 0.93.");
        } else {
            seasonalFactor = 1.0;
            explanation.add("Обычный сезон: коэффициент 1.00.");
        }

        double discountAmount = 0.0;
        if (client != null && client.discount() != null) {
            discountAmount = ((base * hotelMultiplier) + nightsDelta) * client.discount().percent() / 100.0;
            explanation.add(String.format(
                    "Клиентская скидка %s (%d%%): -%.2f руб.",
                    client.discount().name(),
                    client.discount().percent(),
                    discountAmount
            ));
        } else {
            explanation.add("Клиентская скидка не применялась.");
        }

        double fridayDiscount = 0.0;
        if (request.getFridayPromo() && today.getDayOfWeek() == DayOfWeek.FRIDAY) {
            fridayDiscount = (base * hotelMultiplier) * 0.15;
            explanation.add(String.format("Пятничная акция: -%.2f руб.", fridayDiscount));
        } else if (request.getFridayPromo()) {
            fridayDiscount = (base * hotelMultiplier) * 0.05;
            explanation.add(String.format("Запрошена пятничная акция вне пятницы: компромиссная скидка -%.2f руб.", fridayDiscount));
        }

        double serviceFee = 2900.0 + request.getTravelers() * 650.0;
        explanation.add(String.format("Сервисный сбор: %.2f руб.", serviceFee));

        double subtotal = (base * hotelMultiplier)
                + nightsDelta
                + mealPrice
                + transportPrice
                + insurance
                + urgentVisa
                + airportTransfer
                + seaView
                + serviceFee
                - discountAmount
                - fridayDiscount;
        double total = round(subtotal * seasonalFactor);
        double prepayment = round(total * 0.3);
        double monthlyPayment = round((total - prepayment) / 3.0);

        String headline = String.format("Экспресс-предложение по туру %s (%s)", tour.title(), tour.destination());
        explanation.add("Расчет выполнен через God Class ExpressTravelCalculatorGodClass.");
        return new EstimateResult(headline, total, prepayment, monthlyPayment, explanation);
    }

    private static double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
