package ru.bmstu.bank.domain.repository.impl;
import org.springframework.stereotype.Repository;
import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.repository.AccountRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
    private final Map<Long, Account> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Account save(Account account) {
        long id = idGenerator.getAndIncrement();
        store.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }
}