package ru.bmstu.service;

import ru.bmstu.exception.*;
import ru.bmstu.model.*;
import ru.bmstu.observer.*;
import ru.bmstu.repository.*;
import ru.bmstu.service.discount.DiscountStrategy;
import ru.bmstu.service.discount.DiscountStrategyFactory;
import ru.bmstu.service.interfaces.IBookingService;
import ru.bmstu.util.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final ClientRepository clientRepository;
    //private final DiscountStrategy discountStrategy;

    public BookingService(BookingRepository bookingRepository,
                          TourRepository tourRepository,
                          ClientRepository clientRepository) {
        this.bookingRepository = bookingRepository;
        this.tourRepository = tourRepository;
        this.clientRepository = clientRepository;
        //this.discountStrategy = discountStrategy;
    }

    public Booking bookTour(int clientId, int tourId, String strategyName) {
        Client client = clientRepository.findById(clientId)
                .orElse(null);
        Validator.validateClientExists(client, clientId);

        Tour tour = tourRepository.findById(tourId)
                .orElse(null);
        Validator.validateTourExists(tour, tourId);

        DiscountStrategy strategy = DiscountStrategyFactory.getStrategy(strategyName);

        BigDecimal discount = strategy.getDiscountPercentage(tour, client);
        BigDecimal finalPrice = calculatePriceWithDiscount(tour.getBasePrice(), discount);

        System.out.println("Выбрано: " + strategyName);
        System.out.println("Размер скидки: " + discount + "%");

        Booking booking = new Booking(0, clientId, tourId, client.getEmail(), finalPrice);
        Booking saved = bookingRepository.save(booking);

//        saved.attach(new EmailNotifier(client.getEmail()));
//        saved.attach(new ConsoleNotifier("Админ"));
//        saved.attach(new LoggerNotifier());

        return saved;
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

    public List<Booking> getClientBookings(int clientId) {
        return bookingRepository.findByClientId(clientId);
    }

    public void cancelBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        Validator.validateBookingExists(booking, bookingId);

        // состояние отмены брони
        booking.cancel();
        bookingRepository.save(booking);
    }
}