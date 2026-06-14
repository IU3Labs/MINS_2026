package ru.bmstu.travel.core.factory;

import ru.bmstu.travel.core.catalog.CatalogClient;
import ru.bmstu.travel.core.catalog.CatalogTour;
import ru.bmstu.travel.core.model.Booking;

public class BookingFactory {
    public Booking create(String bookingId, CatalogClient client, CatalogTour tour, double finalPrice) {
        return new Booking(
                bookingId,
                client.id(),
                client.fullName(),
                tour.id(),
                tour.title(),
                finalPrice
        );
    }
}
