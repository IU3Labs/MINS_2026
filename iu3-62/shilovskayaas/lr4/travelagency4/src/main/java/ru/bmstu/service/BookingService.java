package ru.bmstu.service;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.grpc.ReferenceServiceClient;
import ru.bmstu.model.Booking;
import ru.bmstu.repository.BookingRepository;
import ru.bmstu.service.discount.DiscountStrategy;
import ru.bmstu.service.discount.DiscountStrategyFactory;
import ru.bmstu.service.interfaces.IBookingService;
import ru.bmstu.util.Logger;
import ru.bmstu.util.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final ReferenceServiceClient refClient;

    public BookingService(BookingRepository bookingRepository,
                          ReferenceServiceClient refClient) {
        this.bookingRepository = bookingRepository;
        this.refClient = refClient;
    }

    private String getTraceId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Booking bookTour(int clientId, int tourId, String strategyName) {
        String traceId = getTraceId();
        Logger.log(traceId, "BookingService: начало бронирования клиент=" + clientId + ", тур=" + tourId);

        try {
            // Получаем информацию о клиенте через gRPC
            ReferenceServiceClient.ClientInfo clientInfo = refClient.getClient(clientId, traceId)
                    .orElseThrow(() -> new TourAgencyException("Клиент с ID " + clientId + " не найден в Service B"));

            // Получаем информацию о туре через gRPC
            ReferenceServiceClient.TourInfo tourInfo = refClient.getTour(tourId, traceId)
                    .orElseThrow(() -> new TourAgencyException("Тур с ID " + tourId + " не найден в Service B"));

            // Выбираем стратегию скидки
            DiscountStrategy strategy = DiscountStrategyFactory.getStrategy(strategyName);

            // Получаем скидку (здесь нужно адаптировать стратегии для работы с TourInfo и ClientInfo)
            BigDecimal discount = getDiscountPercentage(strategy, tourInfo, clientInfo);

            BigDecimal finalPrice = calculatePriceWithDiscount(tourInfo.getBasePrice(), discount);

            Logger.log(traceId, "BookingService: скидка=" + discount + "%, итоговая цена=" + finalPrice);

            // Создаём бронирование
            Booking booking = new Booking(0, clientId, tourId, clientInfo.getEmail(), finalPrice);
            Booking saved = bookingRepository.save(booking);

            Logger.log(traceId, "BookingService: бронирование создано, id=" + saved.getId());
            return saved;

        } catch (Exception e) {
            Logger.log(traceId, "BookingService: ОШИБКА - " + e.getMessage());

            // Проверяем, не ошибка ли это недоступности Service B
            if (e.getMessage().contains("Service B") || e.getMessage().contains("gRPC")) {
                throw new TourAgencyException(
                        "Сервис справочников недоступен. Пожалуйста, убедитесь, что Service B запущен.\n" +
                                "Для запуска Service B выполните в папке travelagency-service-b: mvn compile exec:java -Dexec.mainClass=ru.bmstu.ServiceBMain");
            }
            throw e;
        }
    }

    private BigDecimal getDiscountPercentage(DiscountStrategy strategy,
                                             ReferenceServiceClient.TourInfo tourInfo,
                                             ReferenceServiceClient.ClientInfo clientInfo) {
        // Здесь нужно адаптировать существующие стратегии
        // Пока возвращаем 0
        return BigDecimal.ZERO;
    }

    private BigDecimal calculatePriceWithDiscount(BigDecimal basePrice, BigDecimal discountPercent) {
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return basePrice;
        }

        BigDecimal validDiscount = discountPercent.min(new BigDecimal("100"));
        BigDecimal multiplier = BigDecimal.valueOf(100)
                .subtract(validDiscount)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return basePrice.multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<Booking> getClientBookings(int clientId) {
        return bookingRepository.findByClientId(clientId);
    }

    @Override
    public void cancelBooking(int bookingId) {
        String traceId = getTraceId();
        Logger.log(traceId, "BookingService: отмена бронирования " + bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        Validator.validateBookingExists(booking, bookingId);

        booking.cancel();
        bookingRepository.save(booking);

        Logger.log(traceId, "BookingService: бронирование " + bookingId + " отменено");
    }
}