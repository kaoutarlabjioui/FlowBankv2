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

    private final TransactionRepository transactionRepository;
    private final FeeRuleService feeRuleService;
    private final AccountRepository accountRepository;

    public TransactionServiceImp(TransactionRepository transactionRepository,
                                 FeeRuleService feeRuleService,
                                 AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.feeRuleService = feeRuleService;
        this.accountRepository = accountRepository;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {


        Optional<FeeRule> optionalRule = feeRuleService.findActiveRuleByTransactionType(transaction.getType());
        if (optionalRule.isPresent()) {
            FeeRule rule = optionalRule.get();
            BigDecimal fee = BigDecimal.ZERO;

            if (rule.getMode() == FeeMode.FIX) {
                fee = rule.getValue();
            } else if (rule.getMode() == FeeMode.PERCENT) {
                fee = transaction.getMontant()
                        .multiply(rule.getValue().divide(BigDecimal.valueOf(100)));
            }
            transaction.setFeeAmount(fee);
            transaction.setFeeRuleId(rule.getId());
        }


        switch (transaction.getType()) {
            case DEPOSIT -> validateDeposit(transaction);
            case WITHDRAW -> validateWithdraw(transaction);
            case TRANSFER -> validateTransfer(transaction);
        }

        if (transaction.getType() == TransactionType.TRANSFER &&
                transaction.getCompteDestination() != null &&
                !transaction.getCompteDestination().equals(transaction.getCompteSource())) {
            transaction.setStatus(TransactionStatus.PENDING);

            return transactionRepository.save(transaction);
        } else {

            transaction.setStatus(TransactionStatus.SETTLED);
            return transactionRepository.saveWithAccountUpdate(transaction);
        }
    }



    private void validateDeposit(Transaction transaction) {

        accountRepository.findById(transaction.getCompteDestination())
                .orElseThrow(() -> new IllegalStateException("Destination account not found"));
    }

    private void validateWithdraw(Transaction transaction) {
        Account source = accountRepository.findById(transaction.getCompteSource())
                .orElseThrow(() -> new IllegalStateException("Source account not found"));

        BigDecimal totalDebit = transaction.getMontant().add(transaction.getFeeAmount());
        if (source.getBalance().compareTo(totalDebit) < 0) {
            throw new IllegalStateException("Insufficient balance for withdrawal");
        }
    }

    public void transferToExternalAccount(Account source, BigDecimal amount, String rib) {

        BigDecimal fee = BigDecimal.ZERO;
        Optional<FeeRule> optionalRule = feeRuleService.findActiveRuleByTransactionType(TransactionType.TRANSFER);
        if (optionalRule.isPresent()) {
            FeeRule rule = optionalRule.get();
            if (rule.getMode() == FeeMode.FIX) {
                fee = rule.getValue();
            } else if (rule.getMode() == FeeMode.PERCENT) {
                fee = amount.multiply(rule.getValue().divide(BigDecimal.valueOf(100)));
            }
        }


        BigDecimal totalDebit = amount.add(fee);
        if (source.getBalance().compareTo(totalDebit) < 0) {
            throw new IllegalStateException("Insufficient balance for external transfer (including fees)");
        }


        Transaction tx = new Transaction();
        tx.setType(TransactionType.TRANSFER);
        tx.setMontant(amount);
        tx.setCompteSource(source.getId());
        tx.setDevise(source.getDevise());
        tx.setFeeAmount(fee);
        tx.setStatus(TransactionStatus.PENDING);
        tx.setDescription("External transfer to RIB " + rib);

        transactionRepository.save(tx);

        System.out.println(" Transaction is PENDING and needs manager approval.");
    }


    private void validateTransfer(Transaction transaction) {
        Account source = accountRepository.findById(transaction.getCompteSource())
                .orElseThrow(() -> new IllegalStateException("Source account not found"));

        BigDecimal totalDebit = transaction.getMontant().add(transaction.getFeeAmount());
        if (source.getBalance().compareTo(totalDebit) < 0) {
            throw new IllegalStateException("Insufficient balance for transfer");
        }

        if (transaction.getCompteDestination() != null) {
            accountRepository.findById(transaction.getCompteDestination())
                    .orElseThrow(() -> new IllegalStateException("Destination account not found"));
        }
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

    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.findByStatus(status);
    }

    public void updateTransactionStatus(UUID transactionId, TransactionStatus status) {
        transactionRepository.updateStatus(transactionId, status);
    }

    public void approveTransaction(Transaction transaction) {
        if (transaction.getStatus() != TransactionStatus.PENDING) return;


        transactionRepository.applyTransactionBalances(transaction);
        transaction.setStatus(TransactionStatus.SETTLED);
        transactionRepository.updateStatus(transaction.getId(), TransactionStatus.SETTLED);
    }

    public void rejectTransaction(Transaction transaction) {
        if (transaction.getStatus() != TransactionStatus.PENDING) return;

        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.updateStatus(transaction.getId(), TransactionStatus.FAILED);
    }


}
