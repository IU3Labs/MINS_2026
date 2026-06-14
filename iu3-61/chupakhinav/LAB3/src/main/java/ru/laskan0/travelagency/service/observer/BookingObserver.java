package ru.laskan0.travelagency.service.observer;

import ru.laskan0.travelagency.model.Booking;

public interface BookingObserver {
    void onBookingCreated(Booking booking);

    void onBookingCancelled(Booking booking);
}
