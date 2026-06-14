package ru.bmstu.travel.core.repository;

import ru.bmstu.travel.core.model.Booking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryBookingRepository {
    private final Map<String, Booking> bookings = new LinkedHashMap<>();

    public synchronized Booking save(Booking booking) {
        bookings.put(booking.id(), booking);
        return booking;
    }

    public synchronized Booking findById(String bookingId) {
        return bookings.get(bookingId);
    }

    public synchronized List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}
