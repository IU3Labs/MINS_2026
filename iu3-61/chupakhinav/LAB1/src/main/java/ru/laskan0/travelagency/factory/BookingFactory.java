package ru.laskan0.travelagency.factory;

import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Tour;

public abstract class BookingFactory {
    public final Booking create(String id, Client client, Tour tour, double finalPrice) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("ID бронирования не может быть пустым");
        }
        if (client == null) {
            throw new InvalidDataException("Клиент не может быть null");
        }
        if (tour == null) {
            throw new InvalidDataException("Тур не может быть null");
        }
        return createBooking(id.trim(), client, tour, finalPrice);
    }

    protected abstract Booking createBooking(String id, Client client, Tour tour, double finalPrice);
}
