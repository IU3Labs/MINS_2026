package ru.bmstu.bank.application.service;

import org.springframework.stereotype.Service;
import ru.bmstu.bank.application.pattern.command.CommandInvoker;
import ru.bmstu.bank.application.pattern.command.DepositCommand;
import ru.bmstu.bank.application.pattern.command.WithdrawCommand;
import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.model.NotificationEvent;
import ru.bmstu.bank.domain.model.Transaction;
import ru.bmstu.bank.domain.exception.InsufficientFundsException;
import ru.bmstu.bank.domain.exception.InvalidAmountException;
import ru.bmstu.bank.domain.repository.AccountRepository;
import ru.bmstu.bank.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    private final CommandInvoker commandInvoker;
    private final NotificationService notificationService;
    private final AtomicLong txIdGen = new AtomicLong(5000);

    public TransactionService(AccountRepository accountRepo,
                              TransactionRepository transactionRepo,
                              NotificationService notificationService) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.commandInvoker = new CommandInvoker();
        this.notificationService = notificationService;
    }

    public void depositViaCommand(Long accountId, BigDecimal amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
        var cmd = new DepositCommand(account, amount, transactionRepo, txIdGen);
        commandInvoker.execute(cmd);
        notificationService.notify(accountId, NotificationEvent.EventType.DEPOSIT, amount, "Пополнение");
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(amount, account.getBalance());
        }

        var cmd = new WithdrawCommand(account, amount, transactionRepo, txIdGen);
        commandInvoker.execute(cmd);

        notificationService.notify(accountId, NotificationEvent.EventType.WITHDRAW, amount, "Снятие средств");
    }

    public void applyInterest(Long accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));

        BigDecimal interest = account.getInterestStrategy().calculateInterest(account);

        var cmd = new DepositCommand(account, interest, transactionRepo, txIdGen);
        commandInvoker.execute(cmd);

        notificationService.notify(accountId, NotificationEvent.EventType.INTEREST, interest,
                "Начислены проценты по тарифу: " + account.getInterestStrategy().getDescription());
    }

    public List<Transaction> getStatement(Long accountId) {
        return transactionRepo.findByAccountId(accountId);
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }
}