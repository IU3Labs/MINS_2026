package ru.bmstu.bank.domain.repository;
import ru.bmstu.bank.domain.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(Long id);
    List<Account> findAll();
}