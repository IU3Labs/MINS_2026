package ru.bmstu.model.state;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Booking;

public interface BookingState {

    void cancel(Booking booking) throws TourAgencyException;

    void pay(Booking booking) throws TourAgencyException;

    String getStatusName();

    boolean canBeCancelled();

    boolean canBePaid();
}