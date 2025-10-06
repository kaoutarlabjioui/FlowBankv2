package org.example.controller;

import org.example.entities.Account;
import org.example.entities.Client;
import org.example.entities.Credit;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.CreditStatus;
import org.example.entities.enums.InterestType;
import org.example.service.AccountService;
import org.example.service.ClientService;
import org.example.service.CreditService;
import org.example.utils.ConsoleUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CreditController {

        private CreditService creditService;
        private ClientService clientService;
        private AccountService accountService;

    public CreditController(CreditService creditService, ClientService clientService,AccountService accountService) {
        this.creditService = creditService;
        this.clientService = clientService;
        this.accountService = accountService;

    }
    public void requestCredit() {
        System.out.println("\n=== CREDIT REQUEST ===");

        String cin = ConsoleUtils.readString("Enter client CIN: ");
        Client client = clientService.getClientByCin(cin);

        if (client == null) {
            System.out.println("No client found with CIN: " + cin);
            return;
        }

        BigDecimal montant = ConsoleUtils.readPositiveBigDecimal("Enter credit amount: ");
        int dureeMois = ConsoleUtils.readInt("Enter duration in months: ", 1, 360);
        BigDecimal tauxInteret = ConsoleUtils.readPositiveBigDecimal("Enter interest rate (% per year): ");

        String typeStr = ConsoleUtils.readString("Interest type (SIMPLE / COMPOSE): ").toUpperCase();
        InterestType interestType;
        try {
            interestType = InterestType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid type, defaulting to SIMPLE.");
            interestType = InterestType.SIMPLE;
        }

        boolean success = creditService.requestCredit(client, montant, dureeMois, tauxInteret, interestType);

        BigDecimal mensualite;
        if (interestType == InterestType.SIMPLE) {
            mensualite = montant.add(montant.multiply(tauxInteret).divide(BigDecimal.valueOf(100)))
                    .divide(BigDecimal.valueOf(dureeMois), 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal monthlyRate = tauxInteret.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);
            mensualite = montant.multiply(monthlyRate)
                    .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyRate).pow(-dureeMois)), 2, RoundingMode.HALF_UP);
        }

        System.out.println("Monthly payment for this credit would be: " + mensualite);

        if (success) {
            System.out.println("Credit request submitted for client " + client.getNom() + ". Awaiting manager approval.");
        } else {
            System.out.println("Credit request could not be processed.");
        }
    }



    public void processPendingCredits() {
        List<Credit> pendingCredits = creditService.getCreditsByStatus(CreditStatus.PENDING);
        if (pendingCredits.isEmpty()) {
            System.out.println("No pending credit requests.");
            return;
        }

        for (Credit credit : pendingCredits) {
            System.out.println("=== Credit Request ===");
            System.out.println("Client account ID : " + credit.getAccountId());
            System.out.println("Amount           : " + credit.getMontant());
            System.out.println("Duration (months): " + credit.getDureeMois());
            System.out.println("Interest rate    : " + credit.getTauxInteret() + "%");
            System.out.println("Interest type    : " + credit.getTypeInteret());
            System.out.println("Monthly payment  : " + credit.getMonthlyPayment());
            System.out.println("Status           : " + credit.getStatus());
            System.out.println("-------------------------------");


            boolean approve = ConsoleUtils.readBoolean("Approve this credit  ");
            creditService.processCreditApproval(credit, approve);
        }
    }





}
