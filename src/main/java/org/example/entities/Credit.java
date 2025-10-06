package org.example.entities;

import org.example.entities.enums.CreditStatus;
import org.example.entities.enums.InterestType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Credit {
    private UUID id;
    private BigDecimal montant;
    private int dureeMois;
    private BigDecimal tauxInteret;
    private InterestType typeInteret;
    private LocalDate startDate;
    private LocalDate endDate;
    private CreditStatus status;
    private BigDecimal monthlyPayment;
    private UUID accountId;

    // Constructeurs
    public Credit() {}

    public Credit(UUID id, BigDecimal montant, int dureeMois, BigDecimal tauxInteret,
                  InterestType typeInteret, LocalDate startDate, LocalDate endDate,
                  CreditStatus status, BigDecimal monthlyPayment, UUID accountId) {
        this.id = id;
        this.montant = montant;
        this.dureeMois = dureeMois;
        this.tauxInteret = tauxInteret;
        this.typeInteret = typeInteret;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.monthlyPayment = monthlyPayment;
        this.accountId = accountId;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public int getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }

    public BigDecimal getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(BigDecimal tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public InterestType getTypeInteret() {
        return typeInteret;
    }

    public void setTypeInteret(InterestType typeInteret) {
        this.typeInteret = typeInteret;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", montant=" + montant +
                ", dureeMois=" + dureeMois +
                ", tauxInteret=" + tauxInteret +
                ", typeInteret=" + typeInteret +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", monthlyPayment=" + monthlyPayment +
                ", accountId=" + accountId +
                '}';
    }
}
