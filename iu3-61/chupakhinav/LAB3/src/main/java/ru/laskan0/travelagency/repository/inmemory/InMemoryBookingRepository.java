package ru.laskan0.travelagency.repository.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.repository.BookingRepository;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> bookings = new LinkedHashMap<>();

    @Override
    public Booking save(Booking booking) {
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public boolean existsById(String id) {
        return bookings.containsKey(id);
    }
}
