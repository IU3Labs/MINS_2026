package ru.bmstu.util;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Booking;

import java.math.BigDecimal;

public class Validator {

    public static void validateBookingExists(Booking booking, int bookingId) throws TourAgencyException {
        if (booking == null) {
            throw new TourAgencyException("Бронирование с ID " + bookingId + " не найдено");
        }
    }

    public static void validateNotPaid(boolean isPaid) throws TourAgencyException {
        if (isPaid) {
            throw new TourAgencyException("Бронирование уже оплачено");
        }
    }

    public static void validateIsPaid(boolean isPaid) throws TourAgencyException {
        if (!isPaid) {
            throw new TourAgencyException("Бронирование не было оплачено");
        }
    }
}