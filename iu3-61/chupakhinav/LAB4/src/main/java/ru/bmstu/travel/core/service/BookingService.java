package ru.bmstu.travel.core.service;

import ru.bmstu.travel.core.catalog.CatalogClient;
import ru.bmstu.travel.core.catalog.CatalogTour;
import ru.bmstu.travel.core.exception.BookingStateException;
import ru.bmstu.travel.core.exception.InvalidDataException;
import ru.bmstu.travel.core.exception.NotFoundException;
import ru.bmstu.travel.core.exception.TourAvailabilityException;
import ru.bmstu.travel.core.factory.BookingFactory;
import ru.bmstu.travel.core.gateway.ReferenceGateway;
import ru.bmstu.travel.core.history.TravelAgencyHistoryObserver;
import ru.bmstu.travel.core.model.Booking;
import ru.bmstu.travel.core.model.BookingStatus;
import ru.bmstu.travel.core.pricing.PricingService;
import ru.bmstu.travel.core.repository.InMemoryBookingRepository;

import java.util.List;

public class BookingService {
    private final InMemoryBookingRepository repository;
    private final ReferenceGateway gateway;
    private final PricingService pricingService;
    private final BookingFactory bookingFactory;
    private final TravelAgencyHistoryObserver history;

    public BookingService(ReferenceGateway gateway, TravelAgencyHistoryObserver history) {
        this(new InMemoryBookingRepository(), gateway, new PricingService(), new BookingFactory(), history);
    }

    public BookingService(
            InMemoryBookingRepository repository,
            ReferenceGateway gateway,
            PricingService pricingService,
            BookingFactory bookingFactory,
            TravelAgencyHistoryObserver history
    ) {
        if (gateway == null) {
            throw new InvalidDataException("Шлюз справочного сервиса обязателен.");
        }
        this.repository = repository == null ? new InMemoryBookingRepository() : repository;
        this.gateway = gateway;
        this.pricingService = pricingService == null ? new PricingService() : pricingService;
        this.bookingFactory = bookingFactory == null ? new BookingFactory() : bookingFactory;
        this.history = history == null ? new TravelAgencyHistoryObserver() : history;
    }

    public Booking createBooking(String clientId, String tourId, String traceId) {
        CatalogClient client = gateway.getClient(clientId, traceId);
        if (client == null) {
            throw new NotFoundException("Клиент не найден: " + clientId);
        }
        CatalogTour tour = gateway.getTour(tourId, traceId);
        if (tour == null) {
            throw new NotFoundException("Тур не найден: " + tourId);
        }
        if (tour.availableSeats() <= 0) {
            throw new TourAvailabilityException("В туре не осталось свободных мест.");
        }

        var reserveResult = gateway.reserveTourSeat(tour.id(), traceId);
        if (!reserveResult.success()) {
            throw new TourAvailabilityException(reserveResult.message());
        }

        double finalPrice = pricingService.calculateFinalPrice(tour.basePrice(), client.discount());
        Booking booking = bookingFactory.create(nextId(), client, tour, finalPrice);
        Booking saved = repository.save(booking);
        history.onBookingCreated(saved);
        return saved;
    }

    public Booking cancelBooking(String bookingId, String traceId) {
        Booking booking = repository.findById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Бронирование не найдено: " + bookingId);
        }
        if (booking.status() == BookingStatus.CANCELLED) {
            throw new BookingStateException("Бронирование уже отменено: " + bookingId);
        }
        var releaseResult = gateway.releaseTourSeat(booking.tourId(), traceId);
        if (!releaseResult.success()) {
            throw new BookingStateException(releaseResult.message());
        }
        booking.cancel();
        Booking saved = repository.save(booking);
        history.onBookingCancelled(saved);
        return saved;
    }

    public List<Booking> listBookings() {
        return repository.findAll();
    }

    public List<String> getHistory() {
        return history.getEventLog();
    }

    public void recordEvent(String message) {
        history.record(message);
    }

    private String nextId() {
        int max = repository.findAll().stream()
                .map(Booking::id)
                .filter(id -> id.startsWith("B-") && id.substring(2).matches("\\d+"))
                .mapToInt(id -> Integer.parseInt(id.substring(2)))
                .max()
                .orElse(0);
        return String.format("B-%03d", max + 1);
    }
}
