package org.example.repository;

import org.example.entities.Transaction;
import org.example.entities.enums.TransactionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Transaction saveWithAccountUpdate(Transaction transaction);
    void applyTransactionBalances(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByAccountId(UUID accountId);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findAll();
    void updateStatus(UUID transactionId, TransactionStatus status);
}
