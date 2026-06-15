package ru.bmstu.bank.domain.exception;

public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(Long accountId) {
        super("Счет с ID " + accountId + " не найден.");
    }
}