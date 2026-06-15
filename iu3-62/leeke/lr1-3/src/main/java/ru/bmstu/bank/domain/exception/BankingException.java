package ru.bmstu.bank.domain.exception;

public abstract class BankingException extends RuntimeException {
    protected BankingException(String message) { super(message); }
    protected BankingException(String message, Throwable cause) { super(message, cause); }
}