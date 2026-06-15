package ru.bmstu.bank.domain.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends BankingException {
    public InvalidAmountException(BigDecimal amount) {
        super("Сумма операции должна быть строго больше нуля. Получено: " + amount);
    }
}