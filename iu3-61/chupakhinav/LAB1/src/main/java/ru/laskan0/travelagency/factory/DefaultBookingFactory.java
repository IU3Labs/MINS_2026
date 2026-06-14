package ru.laskan0.travelagency.factory;

import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Tour;

public class DefaultBookingFactory extends BookingFactory {
    @Override
    protected Booking createBooking(String id, Client client, Tour tour, double finalPrice) {
        return new Booking(id, client, tour, finalPrice);
    }
}
