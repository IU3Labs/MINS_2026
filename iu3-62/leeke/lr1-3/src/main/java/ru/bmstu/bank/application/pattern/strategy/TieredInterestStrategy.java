package ru.bmstu.bank.application.pattern.strategy;

import ru.bmstu.bank.domain.model.Account;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TieredInterestStrategy implements InterestStrategy {
    // Порог баланса -> ставка для этого диапазона
    private final NavigableMap<BigDecimal, BigDecimal> tiers = new TreeMap<>();

    public TieredInterestStrategy addTier(BigDecimal minBalance, BigDecimal rate) {
        tiers.put(minBalance, rate);
        return this;
    }

    @Override
    public BigDecimal calculateInterest(Account account) {
        BigDecimal balance = account.getBalance();
        // Находим подходящую ставку: greatest key <= balance
        var entry = tiers.floorEntry(balance);
        if (entry == null) return BigDecimal.ZERO;

        BigDecimal rate = entry.getValue();
        return balance.multiply(rate)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
    }

    @Override
    public String getDescription() {
        return "Прогрессивная ставка: " + tiers.size() + " уровней";
    }
}