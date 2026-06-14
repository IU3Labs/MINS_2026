package com.bank.core.grpc;

import com.bank.contract.AccountDto;
import com.bank.contract.TransactionDto;
import com.bank.core.model.Account;
import com.bank.core.model.Transaction;

public final class ProtoMapper {
    private ProtoMapper() {
    }

    public static AccountDto toProto(Account account) {
        AccountDto.Builder builder = AccountDto.newBuilder()
                .setId(account.getId())
                .setOwnerName(account.getOwnerName())
                .setAccountType(account.getAccountType().name())
                .setBalance(account.getBalance());

        for (Transaction transaction : account.getTransactions()) {
            builder.addTransactions(TransactionDto.newBuilder()
                    .setId(transaction.getId())
                    .setType(transaction.getType().name())
                    .setAmount(transaction.getAmount())
                    .setCreatedAt(transaction.getCreatedAt().toString())
                    .build());
        }
        return builder.build();
    }
}
