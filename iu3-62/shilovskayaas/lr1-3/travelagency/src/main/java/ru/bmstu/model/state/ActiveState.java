package ru.bmstu.model.state;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Booking;

public class ActiveState implements BookingState {

    @Override
    public void cancel(Booking booking) throws TourAgencyException {
        booking.setState(new CancelledState());
        System.out.println("Бронирование ID " + booking.getId() + " отменено");
    }

    @Override
    public void pay(Booking booking) throws TourAgencyException {
        if (!booking.isPaid()) {
            throw new TourAgencyException("Сначала нужно провести оплату через PaymentService");
        }
        // Оплата произойдет, после чего сменим состояние
        booking.setState(new PaidState());
        System.out.println("Бронирование ID " + booking.getId() + " оплачено");
    }

    @Override
    public String getStatusName() {
        return "Активно";
    }

    @Override
    public boolean canBeCancelled() {
        return true;
    }

    @Override
    public boolean canBePaid() {
        return true;
    }
}