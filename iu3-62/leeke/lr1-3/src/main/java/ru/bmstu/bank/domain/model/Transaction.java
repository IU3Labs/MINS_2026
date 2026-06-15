package ru.bmstu.bank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    public enum Status { COMPLETED, CANCELED, REVERSED }

    private final Long id;
    private final Long accountId;
    private final BigDecimal amount;
    private final String type; // DEPOSIT, WITHDRAW, INTEREST, REVERSAL
    private final LocalDateTime date;
    private Status status;

    public Transaction(Long id, Long accountId, BigDecimal amount, String type) {
        this(id, accountId, amount, type, LocalDateTime.now(), Status.COMPLETED);
    }

    public Transaction(Long id, Long accountId, BigDecimal amount, String type, LocalDateTime date, Status status) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getDate() { return date; }
    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }
}