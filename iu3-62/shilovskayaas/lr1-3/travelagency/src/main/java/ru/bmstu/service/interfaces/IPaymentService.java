package ru.bmstu.service.interfaces;

public interface IPaymentService {
    void payOnline(int bookingId, String cardNumber, String expiryDate, String cvv);
    void payOffline(int bookingId, String paymentMethod);
    void refundToCard(int bookingId, String cardNumber);
}
