package org.example.repository;

import org.example.entities.FeeRule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeeRuleRepository {
    FeeRule save(FeeRule feeRule);
    Optional<FeeRule> findById(UUID id);
    List<FeeRule> findAll();
    void update(FeeRule feeRule);
    void deactivate(UUID id); // désactiver la règle
}
