package ru.bmstu.bank.domain.repository.impl;
import org.springframework.stereotype.Repository;
import ru.bmstu.bank.domain.model.Transaction;
import ru.bmstu.bank.domain.repository.TransactionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<Long, Transaction> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Transaction save(Transaction transaction) {
        store.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        return store.values().stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .sorted(Comparator.comparing(Transaction::getDate))
                .collect(Collectors.toList());
    }
}