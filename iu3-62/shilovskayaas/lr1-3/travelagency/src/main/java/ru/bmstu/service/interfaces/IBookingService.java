package ru.bmstu.service.interfaces;

import ru.bmstu.model.Booking;

import java.util.List;

public interface IBookingService {
    Booking bookTour(int clientId, int tourId, String strategyName);
    List<Booking> getClientBookings(int clientId);
    void cancelBooking(int bookingId);
}
