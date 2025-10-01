package org.example.entities;

import org.example.entities.enums.Currency;
import org.example.entities.enums.TransactionStatus;
import org.example.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private TransactionType type;
    private BigDecimal montant;
    private Currency devise;
    private BigDecimal feeAmount;
    private LocalDateTime dateExecution;
    private UUID compteSource;
    private UUID compteDestination;
    private String description;
    private TransactionStatus status;
    private UUID feeRuleId; // Relation avec FeeRule

    public Transaction() {}

    public Transaction(TransactionType type, BigDecimal montant, Currency devise,
                       UUID compteSource, UUID compteDestination, UUID feeRuleId) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.montant = montant;
        this.devise = devise;
        this.feeAmount = BigDecimal.ZERO.setScale(2);
        this.dateExecution = LocalDateTime.now();
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
        this.description = "";
        this.status = TransactionStatus.PENDING;
        this.feeRuleId = feeRuleId;
    }

    // Getters & setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public Currency getDevise() { return devise; }
    public void setDevise(Currency devise) { this.devise = devise; }
    public BigDecimal getFeeAmount() { return feeAmount; }
    public void setFeeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; }
    public LocalDateTime getDateExecution() { return dateExecution; }
    public void setDateExecution(LocalDateTime dateExecution) { this.dateExecution = dateExecution; }
    public UUID getCompteSource() { return compteSource; }
    public void setCompteSource(UUID compteSource) { this.compteSource = compteSource; }
    public UUID getCompteDestination() { return compteDestination; }
    public void setCompteDestination(UUID compteDestination) { this.compteDestination = compteDestination; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public UUID getFeeRuleId() { return feeRuleId; }
    public void setFeeRuleId(UUID feeRuleId) { this.feeRuleId = feeRuleId; }
}
