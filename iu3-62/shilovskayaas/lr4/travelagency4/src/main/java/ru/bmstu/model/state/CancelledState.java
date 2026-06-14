package ru.bmstu.model.state;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Booking;

public class CancelledState implements BookingState {

    @Override
    public void cancel(Booking booking) throws TourAgencyException {
        throw new TourAgencyException("Бронирование уже отменено");
    }

    @Override
    public void pay(Booking booking) throws TourAgencyException {
        throw new TourAgencyException("Нельзя оплатить отмененное бронирование");
    }

    @Override
    public String getStatusName() {
        return "Отменено";
    }

    @Override
    public boolean canBeCancelled() {
        return false;
    }

    @Override
    public boolean canBePaid() {
        return false;
    }
}