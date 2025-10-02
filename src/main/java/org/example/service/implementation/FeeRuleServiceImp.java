package org.example.service.implementation;

import org.example.entities.FeeRule;
import org.example.entities.enums.TransactionType;
import org.example.repository.FeeRuleRepository;
import org.example.service.FeeRuleService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FeeRuleServiceImp implements FeeRuleService {
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

    public List<FeeRule> getAllFeeRules() {
        return feeRuleRepository.findAll();
    }

}



