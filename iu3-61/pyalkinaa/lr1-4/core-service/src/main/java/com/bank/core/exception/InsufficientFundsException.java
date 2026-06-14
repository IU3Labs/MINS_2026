package com.bank.core.exception;

public class InsufficientFundsException extends BankException {
    public InsufficientFundsException(double balance, double amount) {
        super("Недостаточно средств. Баланс: " + balance + ", Требуется: " + amount);
    }
}
