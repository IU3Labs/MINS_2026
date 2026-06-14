package ru.iu3.repository;

import java.util.ArrayList;
import java.util.List;

import ru.iu3.entity.Booking;
import ru.iu3.repository.interfaces.Repository;

public class LBookingRepositorylmpl implements Repository<Booking, Integer> {
    private List<Booking> bookings = new ArrayList<Booking>();

    @Override
    public void add(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public Booking getById(int id) {
        for (Booking booking : bookings) {
            if (booking.getId() == id) {
                return booking;
            }
        }
        return null;
    }

    @Override
    public List<Booking> getAll() {
        return bookings;
    }

    @Override
    public void update(Booking booking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId() == booking.getId()) {
                bookings.set(i, booking);
                return;
            }
        }
    }

}
