package ru.bmstu.bank.application.pattern.command;

import java.math.BigDecimal;

public interface Command {
    void execute();
    void undo(); // опционально: для отмены операции
    String getDescription();
    BigDecimal getAmount();
    Long getAccountId();
}