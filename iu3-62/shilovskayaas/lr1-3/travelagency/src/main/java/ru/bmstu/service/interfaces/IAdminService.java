package ru.bmstu.service.interfaces;

import ru.bmstu.model.Booking;
import ru.bmstu.model.Client;
import ru.bmstu.model.Tour;

import java.math.BigDecimal;
import java.util.List;

public interface IAdminService {
    Tour addDomesticTour(String name, String description, BigDecimal price, String region, String transport);
    Tour addForeignTour(String name, String description, BigDecimal price, String country, boolean needVisa, String currency);
    void removeTour(int tourId);
    void setTourSale(int tourId, BigDecimal discountPercent);
    void setPersonalDiscount(int clientId, BigDecimal discountPercent);
    List<Tour> getAllTours();
    List<Client> getAllClients();
    List<Booking> getAllBookings();
}
