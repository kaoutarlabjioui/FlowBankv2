package org.example.service.implementation;

import org.example.entities.FeeRule;
import org.example.entities.enums.TransactionType;
import org.example.repository.FeeRuleRepository;

import java.util.Optional;
import java.util.UUID;

public class FeeRuleServiceImp {
    private FeeRuleRepository feeRuleRepository;

    public FeeRuleServiceImp(FeeRuleRepository feeRuleRepository) {
        this.feeRuleRepository = feeRuleRepository;
    }

    public FeeRule createFeeRule(FeeRule feeRule) {
        return feeRuleRepository.save(feeRule);
    }

    public Optional<FeeRule> getFeeRuleById(UUID id) {
        return feeRuleRepository.findById(id);
    }

    public void updateFeeRule(FeeRule feeRule) {
        feeRuleRepository.update(feeRule);
    }


    public void deactivateFeeRule(UUID id) {
        feeRuleRepository.deactivate(id);
    }

    public Optional<FeeRule> findActiveRuleByTransactionType(TransactionType type) {
        return feeRuleRepository.findAll()
                .stream()
                .filter(FeeRule::isActive)
                .filter(rule -> rule.getOperationType().equals(type))
                .findFirst();
    }

}
