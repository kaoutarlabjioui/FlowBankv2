package org.example.service;

import org.example.entities.FeeRule;
import org.example.entities.enums.TransactionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeeRuleService {
    FeeRule createFeeRule(FeeRule feeRule);

    Optional<FeeRule> getFeeRuleById(UUID id);

    List<FeeRule> getAllFeeRules();

    void updateFeeRule(FeeRule feeRule);

    void deactivateFeeRule(UUID id);

    Optional<FeeRule> findActiveRuleByTransactionType(TransactionType type);
}
