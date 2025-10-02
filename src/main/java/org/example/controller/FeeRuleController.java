package org.example.controller;

import org.example.entities.FeeRule;
import org.example.entities.enums.Currency;
import org.example.entities.enums.FeeMode;
import org.example.entities.enums.TransactionType;
import org.example.service.FeeRuleService;
import org.example.utils.ConsoleUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FeeRuleController {

    private  FeeRuleService feeRuleService;

    public FeeRuleController(FeeRuleService feeRuleService) {
        this.feeRuleService = feeRuleService;
    }



    public void createFeeRule() {
        System.out.println("\n--- CREATE FEE RULE ---");

        // Choix du type de transaction
        TransactionType[] types = TransactionType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        int typeChoice = ConsoleUtils.readInt("Choose transaction type: ", 1, types.length);
        TransactionType type = types[typeChoice - 1];

        // Choix du mode de frais
        FeeMode[] modes = FeeMode.values();
        for (int i = 0; i < modes.length; i++) {
            System.out.println((i + 1) + ". " + modes[i]);
        }
        int modeChoice = ConsoleUtils.readInt("Choose fee mode: ", 1, modes.length);
        FeeMode mode = modes[modeChoice - 1];

        // Valeur du frais
        BigDecimal value = ConsoleUtils.readPositiveBigDecimal("Enter fee value: ");

        // Devise
        String currency = ConsoleUtils.readString("Enter currency (e.g., MAD, USD, EUR): ").toUpperCase();
        Currency currencyEnum;
        try {
            currencyEnum = Currency.valueOf(currency);
        } catch (IllegalArgumentException e) {
            System.out.println(" Invalid currency. Please enter MAD, USD, or EUR.");
            return;
        }// ou redemander
        FeeRule feeRule = new FeeRule();
        feeRule.setOperationType(type);
        feeRule.setMode(mode);
        feeRule.setValue(value);
        feeRule.setCurrency(currencyEnum);
        feeRule.setActive(true);

        feeRuleService.createFeeRule(feeRule);
        System.out.println(" Fee rule created successfully!");
    }

    public void listFeeRules() {
        System.out.println("\n--- LIST FEE RULES ---");
        List<FeeRule> rules = feeRuleService.getAllFeeRules();
        if (rules.isEmpty()) {
            System.out.println("No fee rules found.");
            return;
        }
        for (FeeRule rule : rules) {
            System.out.println(rule);
        }
    }

    public void updateFeeRule() {
        System.out.println("\n--- UPDATE FEE RULE ---");
        UUID id = UUID.fromString(ConsoleUtils.readString("Enter Fee Rule ID: "));
        Optional<FeeRule> optionalRule = feeRuleService.getFeeRuleById(id);

        if (optionalRule.isEmpty()) {
            System.out.println(" Fee rule not found.");
            return;
        }

        FeeRule rule = optionalRule.get();
        BigDecimal newValue = ConsoleUtils.readPositiveBigDecimal("Enter new fee value: ");
        rule.setValue(newValue);

        feeRuleService.updateFeeRule(rule);
        System.out.println(" Fee rule updated successfully!");
    }

    public void deactivateFeeRule() {
        System.out.println("\n--- DEACTIVATE FEE RULE ---");
        UUID id = UUID.fromString(ConsoleUtils.readString("Enter Fee Rule ID: "));
        feeRuleService.deactivateFeeRule(id);
        System.out.println(" Fee rule deactivated successfully!");
    }
}
