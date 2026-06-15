package ru.bmstu.bank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NotificationEvent {
    public enum EventType { DEPOSIT, WITHDRAW, INTEREST, LOW_BALANCE, ACCOUNT_CREATED }

    private final Long accountId;
    private final EventType type;
    private final BigDecimal amount;
    private final String message;
    private final LocalDateTime timestamp;

    public NotificationEvent(Long accountId, EventType type, BigDecimal amount, String message) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getAccountId() { return accountId; }
    public EventType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}