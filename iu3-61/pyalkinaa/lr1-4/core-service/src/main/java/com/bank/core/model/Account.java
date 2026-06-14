package com.bank.core.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final long id;
    private final String ownerName;
    private final AccountType accountType;
    private double balance;
    private final List<Transaction> transactions;

    public Account(long id, String ownerName, AccountType accountType) {
        this.id = id;
        this.ownerName = ownerName;
        this.accountType = accountType;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction(transactions.size() + 1L, TransactionType.DEPOSIT, amount));
    }

    public void withdraw(double amount) {
        balance -= amount;
        transactions.add(new Transaction(transactions.size() + 1L, TransactionType.WITHDRAW, amount));
    }

    public void accrueInterest(double amount) {
        balance += amount;
        transactions.add(new Transaction(transactions.size() + 1L, TransactionType.INTEREST, amount));
    }
}
