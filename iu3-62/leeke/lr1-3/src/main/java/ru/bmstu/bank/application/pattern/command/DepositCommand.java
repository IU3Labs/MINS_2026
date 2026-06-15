package ru.bmstu.bank.application.pattern.command;

import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.model.Transaction;
import ru.bmstu.bank.domain.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class DepositCommand implements Command {
    private final Account account;
    private final BigDecimal amount;
    private final TransactionRepository transactionRepo;
    private final AtomicLong txIdGen;

    private Transaction executedTransaction;
    private BigDecimal previousBalance;

    public DepositCommand(Account account, BigDecimal amount,
                          TransactionRepository transactionRepo, AtomicLong txIdGen) {
        this.account = account;
        this.amount = amount;
        this.transactionRepo = transactionRepo;
        this.txIdGen = txIdGen;
    }

    @Override
    public void execute() {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        previousBalance = account.getBalance();
        account.deposit(amount);

        executedTransaction = new Transaction(
                txIdGen.getAndIncrement(),
                account.getId(),
                amount,
                "DEPOSIT"
        );
        transactionRepo.save(executedTransaction);
    }

    @Override
    public void undo() {
        if (executedTransaction != null) {
            account.withdraw(amount);
            executedTransaction.setStatus(Transaction.Status.CANCELED);
            Transaction reversal = new Transaction(
                    txIdGen.getAndIncrement(),
                    account.getId(),
                    amount.negate(),
                    "REVERSAL",
                    LocalDateTime.now(),
                    Transaction.Status.REVERSED
            );
            transactionRepo.save(reversal);
        }
    }

    @Override
    public String getDescription() {
        return "Deposit to " + account.getId() + " for " + amount + " руб.";
    }

    @Override
    public BigDecimal getAmount() { return amount; }

    @Override
    public Long getAccountId() { return account.getId(); }

    public Transaction getTransaction() { return executedTransaction; }
}