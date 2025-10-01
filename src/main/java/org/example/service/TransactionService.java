package org.example.service;

import org.example.entities.Account;
import org.example.entities.Transaction;
import org.example.entities.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);

    Optional<Transaction> getTransactionById(UUID id);

    List<Transaction> getTransactionsByAccountId(UUID accountId);

    List<Transaction> getAllTransactions();

    void updateTransactionStatus(UUID transactionId, TransactionStatus status);
    void transferToExternalAccount(Account source, BigDecimal amount, String rib);
}
