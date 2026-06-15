package ru.bmstu.bank.application.service;

import org.springframework.stereotype.Service;
import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.repository.AccountRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService {
    private final AccountRepository repository;
    private final AtomicLong idGen = new AtomicLong(1000);

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(String ownerName) {
        return repository.save(new Account(idGen.getAndIncrement(), ownerName));
    }

    public void changeInterestStrategy(Long accountId, ru.bmstu.bank.application.pattern.strategy.InterestStrategy strategy) {
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
        account.setInterestStrategy(strategy);
    }

    public Account getAccount(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }
}