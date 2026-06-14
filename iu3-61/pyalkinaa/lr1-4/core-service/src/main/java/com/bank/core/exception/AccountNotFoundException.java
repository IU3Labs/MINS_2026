package com.bank.core.exception;

public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(long accountId) {
        super("Счет с ID " + accountId + " не найден.");
    }
}
