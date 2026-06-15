package ru.bmstu.bank.domain.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends BankingException {
    public InsufficientFundsException(BigDecimal requested, BigDecimal available) {
        super("Недостаточно средств. Требуется: " + requested + ", Доступно: " + available);
    }
}