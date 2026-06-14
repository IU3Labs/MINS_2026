package ru.bmstu.model.state;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Booking;

public class PaidState implements BookingState {

    @Override
    public void cancel(Booking booking) throws TourAgencyException {
        throw new TourAgencyException("Нельзя отменить оплаченное бронирование. Обратитесь в поддержку для возврата.");
    }

    @Override
    public void pay(Booking booking) throws TourAgencyException {
        throw new TourAgencyException("Бронирование уже оплачено");
    }

    @Override
    public String getStatusName() {
        return "Оплачено";
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