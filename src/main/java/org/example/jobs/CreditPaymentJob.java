package org.example.jobs;


import org.example.entities.Account;
import org.example.entities.Credit;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.CreditStatus;
import org.example.service.AccountService;
import org.example.service.CreditService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreditPaymentJob implements Runnable {

        private final CreditService creditService;
        private final AccountService accountService;

        public CreditPaymentJob(CreditService creditService, AccountService accountService) {
            this.creditService = creditService;
            this.accountService = accountService;
        }

        @Override
        public void run() {
            System.out.println("=== Running Credit Payment Job ===");

            List<Credit> credits = creditService.getCreditsByStatus(CreditStatus.APPROVED);

            for (Credit credit : credits) {

                UUID clientId = accountService.getAccount(credit.getAccountId())
                        .map(Account::getClientId)
                        .orElse(null);

                if (clientId == null) continue;

                Account currentAccount = accountService.getAccountsByClient(clientId).stream()
                        .filter(a -> a.getType() == AccountType.Courant)
                        .findFirst()
                        .orElse(null);

                Account creditAccount = accountService.getAccount(credit.getAccountId()).orElse(null);

                if (currentAccount == null || creditAccount == null) continue;

                BigDecimal monthlyPayment = credit.getMonthlyPayment();


                if (currentAccount.getBalance().compareTo(monthlyPayment) >= 0) {
                    currentAccount.setBalance(currentAccount.getBalance().subtract(monthlyPayment));
                    creditAccount.setBalance(creditAccount.getBalance().add(monthlyPayment));

                    accountService.updateAccount(currentAccount);
                    accountService.updateAccount(creditAccount);

                    System.out.println("Paiement effectué pour crédit " + credit.getId() + " -> " + monthlyPayment);
                } else {
                    System.out.println(" Client " + clientId + " n’a pas assez d’argent pour payer la mensualité.");

                }
            }
        }
    }


