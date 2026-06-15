package ru.bmstu.bank.application.pattern.strategy;

import ru.bmstu.bank.domain.model.Account;
import java.math.BigDecimal;

public interface InterestStrategy {
    BigDecimal calculateInterest(Account account);
    String getDescription();
}