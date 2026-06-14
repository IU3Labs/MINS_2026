package com.bank.core.model;

import java.time.LocalDateTime;

public class Transaction {
    private final long id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime createdAt;

    public Transaction(long id, TransactionType type, double amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
