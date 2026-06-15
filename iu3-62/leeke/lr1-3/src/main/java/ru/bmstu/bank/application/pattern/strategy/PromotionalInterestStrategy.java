package ru.bmstu.bank.application.pattern.strategy;

import ru.bmstu.bank.domain.model.Account;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class PromotionalInterestStrategy implements InterestStrategy {
    private final BigDecimal promoRate;
    private final LocalDateTime validUntil;
    private final BigDecimal baseRate; // ставка после окончания промо

    public PromotionalInterestStrategy(BigDecimal promoRate, LocalDateTime validUntil, BigDecimal baseRate) {
        this.promoRate = promoRate;
        this.validUntil = validUntil;
        this.baseRate = baseRate;
    }

    @Override
    public BigDecimal calculateInterest(Account account) {
        BigDecimal currentRate = LocalDateTime.now().isBefore(validUntil) ? promoRate : baseRate;
        return account.getBalance()
                .multiply(currentRate)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
    }

    @Override
    public String getDescription() {
        return "Промо-тариф до " + validUntil.toLocalDate() +
                (LocalDateTime.now().isBefore(validUntil) ? " (активен)" : " (завершен)");
    }
}