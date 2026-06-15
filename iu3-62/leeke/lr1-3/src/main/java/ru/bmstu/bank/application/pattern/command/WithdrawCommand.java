package ru.bmstu.bank.application.pattern.command;

import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.model.Transaction;
import ru.bmstu.bank.domain.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class WithdrawCommand implements Command {
    private final Account account;
    private final BigDecimal amount;
    private final TransactionRepository transactionRepo;
    private final AtomicLong txIdGen;

    private Transaction executedTransaction;
    private boolean isUndone = false;

    public WithdrawCommand(Account account, BigDecimal amount,
                           TransactionRepository transactionRepo, AtomicLong txIdGen) {
        this.account = account;
        this.amount = amount;
        this.transactionRepo = transactionRepo;
        this.txIdGen = txIdGen;
    }

    @Override
    public void execute() {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть положительной");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете");
        }

        account.withdraw(amount);
        executedTransaction = new Transaction(
                txIdGen.getAndIncrement(),
                account.getId(),
                amount,
                "WITHDRAW",
                LocalDateTime.now(),
                Transaction.Status.COMPLETED
        );
        transactionRepo.save(executedTransaction);
    }

    @Override
    public void undo() {
        if (executedTransaction == null || isUndone) return;
        account.deposit(amount);
        executedTransaction.setStatus(Transaction.Status.CANCELED);
        Transaction reversal = new Transaction(
                txIdGen.getAndIncrement(),
                account.getId(),
                amount,
                "REVERSAL",
                LocalDateTime.now(),
                Transaction.Status.REVERSED
        );
        transactionRepo.save(reversal);

        isUndone = true;
    }

    @Override
    public String getDescription() {
        return "Снятие со счета #" + account.getId() + " суммы " + amount + " руб.";
    }

    @Override
    public BigDecimal getAmount() { return amount; }
    @Override
    public Long getAccountId() { return account.getId(); }
}