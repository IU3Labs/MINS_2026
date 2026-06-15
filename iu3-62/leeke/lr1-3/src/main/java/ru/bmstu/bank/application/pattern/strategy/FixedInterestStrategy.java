package ru.bmstu.bank.application.pattern.strategy;

import ru.bmstu.bank.domain.model.Account;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedInterestStrategy implements InterestStrategy {
    private final BigDecimal annualRate; // например, 0.05 для 5%

    public FixedInterestStrategy(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    @Override
    public BigDecimal calculateInterest(Account account) {
        return account.getBalance()
                .multiply(annualRate)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP); // месячные проценты
    }

    @Override
    public String getDescription() {
        return "Фиксированная ставка: " + annualRate.multiply(BigDecimal.valueOf(100)) + "% годовых";
    }
}