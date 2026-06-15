package ru.bmstu.bank.domain.model;

import ru.bmstu.bank.application.pattern.strategy.FixedInterestStrategy;
import ru.bmstu.bank.application.pattern.strategy.InterestStrategy;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private static final InterestStrategy DEFAULT_STRATEGY = new FixedInterestStrategy(BigDecimal.valueOf(0.05));

    private final Long id;
    private final String ownerName;
    private BigDecimal balance;
    private final LocalDateTime createdAt;
    private InterestStrategy interestStrategy;

    public Account(Long id, String ownerName) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.interestStrategy = DEFAULT_STRATEGY;
    }

    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public InterestStrategy getInterestStrategy() { return interestStrategy; }

    public void setInterestStrategy(InterestStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("Стратегия не может быть null");
        this.interestStrategy = strategy;
    }

    public void deposit(BigDecimal amount) { this.balance = this.balance.add(amount); }
    public void withdraw(BigDecimal amount) { this.balance = this.balance.subtract(amount); }
    public void addInterest(BigDecimal interest) { this.balance = this.balance.add(interest); }
}