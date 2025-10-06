package org.example.repository;

import org.example.entities.Credit;
import org.example.entities.enums.CreditStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditRepository {
    void save(Credit credit);
    Optional<Credit> findById(UUID id);
    List<Credit> findAll();
    List<Credit> findByAccountId(UUID accountId);
    List<Credit> findByStatus(CreditStatus status);
    void updateStatus(UUID id, CreditStatus status);
}
