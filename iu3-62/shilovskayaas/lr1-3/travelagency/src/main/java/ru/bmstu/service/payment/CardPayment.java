package ru.bmstu.service.payment;

import java.math.BigDecimal;

public interface CardPayment {
    void payByCard(BigDecimal amount, String cardNumber, String expiryDate, String cvv);
    void refundToCard(BigDecimal amount, String cardNumber);
}
