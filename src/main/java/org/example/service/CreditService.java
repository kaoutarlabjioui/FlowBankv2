package org.example.service;

import org.example.entities.Client;
import org.example.entities.Credit;
import org.example.entities.enums.CreditStatus;
import org.example.entities.enums.InterestType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditService {
    boolean requestCredit(Client client, BigDecimal montant, int dureeMois, BigDecimal tauxInteret, InterestType interestType);
    Optional<Credit> getCreditById(UUID id);
    List<Credit> getAllCredits();
    List<Credit> getCreditsByStatus(CreditStatus status);
    void processCreditApproval(Credit credit, boolean approve);
}
