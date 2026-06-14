package ru.bmstu.service;

import ru.bmstu.exception.*;
import ru.bmstu.model.*;
import ru.bmstu.repository.*;
import ru.bmstu.service.interfaces.IPaymentService;
import ru.bmstu.service.payment.*;
import ru.bmstu.util.Validator;

import java.util.Scanner;

public class PaymentService implements IPaymentService {
    private final BookingRepository bookingRepository;
    private final Scanner scanner;

    public PaymentService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.scanner = new Scanner(System.in);
    }

    public void payOnline(int bookingId, String cardNumber, String expiryDate, String cvv) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        Validator.validateBookingExists(booking, bookingId);
        Validator.validateNotPaid(booking.isPaid());


        if (!booking.canBePaid()) {
            throw new PaymentException("Бронирование в статусе '" + booking.getStatusName() + "' нельзя оплатить");
        }

        CardPayment onlinePayment = new OnlinePayment();
        onlinePayment.payByCard(booking.getFinalPrice(), cardNumber, expiryDate, cvv);

        booking.setPaid(true);
        bookingRepository.save(booking);

        System.out.println("Бронирование ID " + bookingId + " успешно оплачено!");
    }

    public void payOffline(int bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        Validator.validateBookingExists(booking, bookingId);

        if (!booking.canBePaid()) {
            throw new PaymentException("Нельзя оплатить бронирование в статусе '" + booking.getStatusName() + "'");
        }

        if (booking.isPaid()) {
            throw new PaymentException("Бронирование уже оплачено");
        }

        OfflinePayment offlinePayment = new OfflinePayment();

        if (paymentMethod.equalsIgnoreCase("card")) {
            System.out.print("Введите номер карты: ");
            String cardNumber = scanner.nextLine();
            System.out.print("Введите срок действия (MM/YY): ");
            String expiryDate = scanner.nextLine();
            System.out.print("Введите CVV: ");
            String cvv = scanner.nextLine();

            offlinePayment.payByCard(booking.getFinalPrice(), cardNumber, expiryDate, cvv);

        } else if (paymentMethod.equalsIgnoreCase("cash")) {
            offlinePayment.payByCash(booking.getFinalPrice());

        } else {
            throw new PaymentException("Неизвестный метод оплаты");
        }

        booking.setPaid(true);
        bookingRepository.save(booking);

        System.out.println("Бронирование ID " + bookingId + " успешно оплачено!");
    }

    public void refundToCard(int bookingId, String cardNumber) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        Validator.validateBookingExists(booking, bookingId);
        Validator.validateIsPaid(booking.isPaid());

        CardPayment payment = new OnlinePayment();
        payment.refundToCard(booking.getFinalPrice(), cardNumber);
    }
}