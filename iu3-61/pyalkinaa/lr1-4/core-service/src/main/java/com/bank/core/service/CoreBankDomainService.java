package com.bank.core.service;

import com.bank.core.exception.AccountNotFoundException;
import com.bank.core.exception.InsufficientFundsException;
import com.bank.core.model.Account;
import com.bank.core.model.AccountType;
import com.bank.core.reference.InterestRule;
import com.bank.core.reference.ReferenceDataClient;
import com.bank.core.repository.AccountRepository;

import java.util.List;

public class CoreBankDomainService {
    private final AccountRepository repository;
    private final ReferenceDataClient referenceDataClient;
    private long nextId;

    public CoreBankDomainService(AccountRepository repository, ReferenceDataClient referenceDataClient) {
        this.repository = repository;
        this.referenceDataClient = referenceDataClient;
        this.nextId = repository.findAll().stream().mapToLong(Account::getId).max().orElse(0L) + 1;
    }

    public Account createAccount(String ownerName, String accountTypeRaw, String traceId) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Имя владельца не может быть пустым");
        }

        String normalizedType = referenceDataClient.validateAccountType(accountTypeRaw, traceId);
        Account account = new Account(nextId++, ownerName.trim(), AccountType.fromString(normalizedType));
        repository.save(account);
        return account;
    }

    public Account deposit(long accountId, double amount, String traceId) {
        referenceDataClient.validateAmount(amount, "deposit", traceId);
        Account account = getAccountOrThrow(accountId);
        account.deposit(amount);
        repository.save(account);
        return account;
    }

    public Account withdraw(long accountId, double amount, String traceId) {
        referenceDataClient.validateAmount(amount, "withdraw", traceId);
        Account account = getAccountOrThrow(accountId);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(account.getBalance(), amount);
        }
        account.withdraw(amount);
        repository.save(account);
        return account;
    }

    public Account accrueInterest(long accountId, double basePercent, String traceId) {
        referenceDataClient.validateAmount(basePercent, "interest", traceId);
        Account account = getAccountOrThrow(accountId);
        InterestRule rule = referenceDataClient.getInterestRule(
                account.getAccountType().name(),
                basePercent,
                account.getBalance(),
                traceId
        );
        double interestAmount = account.getBalance() * (rule.effectivePercent() / 100.0) + rule.bonusAmount();
        account.accrueInterest(interestAmount);
        repository.save(account);
        return account;
    }

    public Account getAccountStatement(long accountId) {
        return getAccountOrThrow(accountId);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    private Account getAccountOrThrow(long accountId) {
        return repository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
