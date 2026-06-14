package ru.bmstu.service.payment;

import java.math.BigDecimal;

public interface CashPayment {
    void payByCash(BigDecimal amount);
    void giveChange(BigDecimal amount); //сдача
}
