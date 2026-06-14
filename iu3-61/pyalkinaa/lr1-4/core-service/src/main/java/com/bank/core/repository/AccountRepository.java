package com.bank.core.repository;

import com.bank.core.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findById(long id);
    List<Account> findAll();
}
