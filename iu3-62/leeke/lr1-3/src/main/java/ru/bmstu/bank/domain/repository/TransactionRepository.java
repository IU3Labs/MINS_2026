package ru.bmstu.bank.domain.repository;
import ru.bmstu.bank.domain.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountId(Long accountId);
}