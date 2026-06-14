package ru.laskan0.travelagency.service;

import java.util.List;

import ru.laskan0.travelagency.exception.DuplicateEntityException;
import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.exception.NotFoundException;
import ru.laskan0.travelagency.exception.TourAvailabilityException;
import ru.laskan0.travelagency.factory.BookingFactory;
import ru.laskan0.travelagency.factory.DefaultBookingFactory;
import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Tour;
import ru.laskan0.travelagency.repository.BookingRepository;
import ru.laskan0.travelagency.service.observer.TravelAgencyObserver;
import ru.laskan0.travelagency.util.IdGenerator;

public class BookingService {
    private static final String BOOKING_ID_PREFIX = "B-";

    private final BookingRepository bookingRepository;
    private final ClientService clientService;
    private final TourService tourService;
    private final PricingService pricingService;
    private final BookingFactory bookingFactory;
    private final List<TravelAgencyObserver> observers;

    public BookingService(
            BookingRepository bookingRepository,
            ClientService clientService,
            TourService tourService,
            PricingService pricingService) {
        this(bookingRepository, clientService, tourService, pricingService, new DefaultBookingFactory(), List.of());
    }

    public BookingService(
            BookingRepository bookingRepository,
            ClientService clientService,
            TourService tourService,
            PricingService pricingService,
            List<TravelAgencyObserver> observers) {
        this(bookingRepository, clientService, tourService, pricingService, new DefaultBookingFactory(), observers);
    }

    public BookingService(
            BookingRepository bookingRepository,
            ClientService clientService,
            TourService tourService,
            PricingService pricingService,
            BookingFactory bookingFactory,
            List<TravelAgencyObserver> observers) {
        if (bookingRepository == null) {
            throw new InvalidDataException("Репозиторий бронирований не может быть null");
        }
        if (clientService == null) {
            throw new InvalidDataException("Сервис клиентов не может быть null");
        }
        if (tourService == null) {
            throw new InvalidDataException("Сервис туров не может быть null");
        }
        if (pricingService == null) {
            throw new InvalidDataException("Сервис ценообразования не может быть null");
        }
        if (bookingFactory == null) {
            throw new InvalidDataException("Фабрика бронирований не может быть null");
        }
        if (observers == null) {
            throw new InvalidDataException("Список наблюдателей не может быть null");
        }
        this.bookingRepository = bookingRepository;
        this.clientService = clientService;
        this.tourService = tourService;
        this.pricingService = pricingService;
        this.bookingFactory = bookingFactory;
        this.observers = List.copyOf(observers);
    }

    public Booking createBooking(String clientId, String tourId) {
        Client client = clientService.getClientById(clientId);
        Tour tour = tourService.getTourById(tourId);
        return createBooking(client, tour);
    }

    public Booking createBooking(Client client, Tour tour) {
        String bookingId = generateNextBookingId();
        return createBooking(bookingId, client, tour);
    }

    public Booking createBooking(String bookingId, String clientId, String tourId) {
        Client client = clientService.getClientById(clientId);
        Tour tour = tourService.getTourById(tourId);
        return createBooking(bookingId, client, tour);
    }

    public Booking createBooking(String bookingId, Client client, Tour tour) {
        if (bookingRepository.existsById(bookingId)) {
            throw new DuplicateEntityException("Бронирование с таким ID уже существует: " + bookingId);
        }
        if (client == null) {
            throw new InvalidDataException("Клиент для бронирования не может быть null");
        }
        if (tour == null) {
            throw new InvalidDataException("Тур для бронирования не может быть null");
        }
        if (!tour.hasAvailableSeats()) {
            throw new TourAvailabilityException("Невозможно создать бронирование: свободных мест нет");
        }

        double finalPrice = pricingService.calculateFinalPrice(tour.getBasePrice(), client.getDiscount());

        tour.reserveSeat();
        try {
            Booking booking = bookingFactory.create(bookingId, client, tour, finalPrice);
            Booking savedBooking = bookingRepository.save(booking);
            notifyBookingCreated(savedBooking);
            return savedBooking;
        } catch (RuntimeException exception) {
            tour.releaseSeat();
            throw exception;
        }
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено: " + bookingId));
        booking.cancel();
        booking.getTour().releaseSeat();
        Booking savedBooking = bookingRepository.save(booking);
        notifyBookingCancelled(savedBooking);
    }

    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено: " + bookingId));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    private void notifyBookingCreated(Booking booking) {
        for (TravelAgencyObserver observer : observers) {
            observer.onBookingCreated(booking);
        }
    }

    private void notifyBookingCancelled(Booking booking) {
        for (TravelAgencyObserver observer : observers) {
            observer.onBookingCancelled(booking);
        }
    }

    private String generateNextBookingId() {
        return IdGenerator.nextId(
                bookingRepository.findAll().stream().map(Booking::getId).toList(),
                BOOKING_ID_PREFIX);
    }
}
