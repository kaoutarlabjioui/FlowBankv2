package org.example.service.implementation;

import org.example.entities.Account;
import org.example.entities.FeeRule;
import org.example.entities.Transaction;
import org.example.entities.enums.FeeMode;
import org.example.entities.enums.TransactionStatus;
import org.example.entities.enums.TransactionType;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.service.FeeRuleService;
import org.example.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionServiceImp implements TransactionService {

    private  TransactionRepository transactionRepository;
    private  FeeRuleService feeRuleService;
    private final AccountRepository accountRepository;

    public TransactionServiceImp(TransactionRepository transactionRepository, FeeRuleService feeRuleService,  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.feeRuleService = feeRuleService;
        this.accountRepository = accountRepository;
    }


    public Transaction createTransaction(Transaction transaction) {
        // Appliquer règle de frais
        Optional<FeeRule> optionalRule = feeRuleService.findActiveRuleByTransactionType(transaction.getType());

        if (optionalRule.isPresent()) {
            FeeRule rule = optionalRule.get();
            BigDecimal fee = BigDecimal.ZERO;

            if (rule.getMode() == FeeMode.FIX) {
                fee = rule.getValue();
            } else if (rule.getMode() == FeeMode.PERCENT) {
                fee = transaction.getMontant().multiply(rule.getValue().divide(BigDecimal.valueOf(100)));
            }

            transaction.setFeeAmount(fee);
            transaction.setFeeRuleId(rule.getId());
        }

        // Appliquer la logique métier (impact sur comptes)
        switch (transaction.getType()) {
            case DEPOSIT -> handleDeposit(transaction);
            case WITHDRAW -> handleWithdraw(transaction);
            case TRANSFER -> handleTransfer(transaction);
        }

        transaction.setStatus(TransactionStatus.SETTLED);
        return transactionRepository.save(transaction);
    }


    private void handleDeposit(Transaction transaction) {
        Account destination = accountRepository.findById(transaction.getCompteDestination()).orElseThrow();
        destination.deposit(transaction.getMontant());
        accountRepository.update(destination);
    }

    private void handleWithdraw(Transaction transaction) {
        Account source = accountRepository.findById(transaction.getCompteSource()).orElseThrow();
        source.withdraw(transaction.getMontant().add(transaction.getFeeAmount())); // inclure les frais
        accountRepository.update(source);
    }

    private void handleTransfer(Transaction transaction) {
        Account source = accountRepository.findById(transaction.getCompteSource()).orElseThrow();

        if (transaction.getCompteDestination() != null) {
            // Transfert interne (même banque)
            Account destination = accountRepository.findById(transaction.getCompteDestination()).orElseThrow();
            source.transfer(transaction.getMontant().add(transaction.getFeeAmount()), destination);
            accountRepository.update(source);
            accountRepository.update(destination);
        }
    }

    public void transferToExternalAccount(Account source, BigDecimal amount, String rib) {
        // Vérifier le solde
        if (source.getBalance().compareTo(amount) < 0) {
            System.out.println(" Insufficient balance.");
            return;
        }

        // Retirer le montant (et les frais si applicable)
        source.withdraw(amount);
        accountRepository.update(source);

        // Créer la transaction pour le suivi
        Transaction tx = new Transaction();
        tx.setType(TransactionType.TRANSFER);
        tx.setMontant(amount);
        tx.setCompteSource(source.getId());
        tx.setDevise(source.getDevise());
        tx.setStatus(TransactionStatus.SETTLED);
        transactionRepository.save(tx);

        System.out.println("✅ External transfer processed successfully!");
    }


    public Optional<Transaction> getTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }


    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    public void updateTransactionStatus(UUID transactionId, TransactionStatus status) {
        transactionRepository.updateStatus(transactionId, status);
    }





}
