package ru.bmstu.repository;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Booking;

import java.util.*;

public class BookingRepository implements Repository<Booking, Integer> {
    private final Map<Integer, Booking> bookings = new HashMap<>();
    private int currentId = 1;  // счетчик

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == 0) {
            booking.setId(currentId++);
            bookings.put(booking.getId(), booking);
        } else {
            bookings.put(booking.getId(), booking);
        }
        return booking;
    }

    @Override
    public Optional<Booking> findById(Integer id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> sorted = new ArrayList<>(bookings.values());
        sorted.sort(Comparator.comparingInt(Booking::getId));
        return sorted;
    }

    @Override
    public void deleteById(Integer id) throws TourAgencyException {
        if (!bookings.containsKey(id)) {
            throw new TourAgencyException("Бронирование с ID " + id + " не найдено");
        }
        bookings.remove(id);
    }

    public List<Booking> findByClientId(int clientId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getClientId() == clientId) {
                result.add(booking);
            }
        }
        result.sort(Comparator.comparingInt(Booking::getId));
        return result;
    }

    public List<Booking> findByTourId(int tourId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getTourId() == tourId) {
                result.add(booking);
            }
        }
        result.sort(Comparator.comparingInt(Booking::getId));
        return result;
    }
}