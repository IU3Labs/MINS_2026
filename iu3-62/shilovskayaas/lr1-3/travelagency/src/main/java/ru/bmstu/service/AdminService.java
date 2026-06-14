package ru.bmstu.service;

import ru.bmstu.exception.*;
import ru.bmstu.model.*;
import ru.bmstu.repository.*;
import ru.bmstu.service.interfaces.IAdminService;
import ru.bmstu.util.Validator;

import java.math.BigDecimal;
import java.util.List;

public class AdminService implements IAdminService {
    private final TourRepository tourRepository;
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;

    public AdminService(TourRepository tourRepository,
                        ClientRepository clientRepository,
                        BookingRepository bookingRepository) {
        this.tourRepository = tourRepository;
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
    }

    public Tour addDomesticTour(String name, String description, BigDecimal price,
                                String region, String transport){
        Validator.validatePrice(price);

        DomesticTour tour = new DomesticTour(0, name, description, price, region, transport);
        return tourRepository.save(tour);
    }

    public Tour addForeignTour(String name, String description, BigDecimal price,
                               String country, boolean needVisa, String currency) {
        Validator.validatePrice(price);

        ForeignTour tour = new ForeignTour(0, name, description, price, country, needVisa, currency);
        return tourRepository.save(tour);
    }

    public void removeTour(int tourId) {
        List<Booking> bookings = bookingRepository.findByTourId(tourId);
        tourRepository.deleteById(tourId);
    }

    public void setTourSale(int tourId, BigDecimal discountPercent) {
        Validator.validateDiscountPercent(discountPercent);

        Tour tour = tourRepository.findById(tourId)
                .orElse(null);
        Validator.validateTourExists(tour, tourId);

        if (discountPercent.compareTo(BigDecimal.ZERO) > 0) {
            tour.setOnSale(true);
            tour.setSalePercentage(discountPercent);
        } else {
            tour.setOnSale(false);
            tour.setSalePercentage(BigDecimal.ZERO);
        }
        tourRepository.save(tour);
    }

    public void setPersonalDiscount(int clientId, BigDecimal discountPercent) {
        Validator.validateDiscountPercent(discountPercent);

        Client client = clientRepository.findById(clientId)
                .orElse(null);
        Validator.validateClientExists(client, clientId);

        client.setPersonalDiscount(discountPercent);
        clientRepository.save(client);
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}