package org.example.service.implementation;

import org.example.entities.Account;
import org.example.entities.Client;
import org.example.entities.Credit;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.CreditStatus;
import org.example.entities.enums.InterestType;
import org.example.repository.CreditRepository;
import org.example.service.AccountService;
import org.example.service.ClientService;
import org.example.service.CreditService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreditServiceImp implements CreditService {

    private CreditRepository creditRepository;
    private ClientService clientService;
    private  AccountService accountService;

    public CreditServiceImp(CreditRepository creditRepository,AccountService accountService,ClientService clientService) {
        this.creditRepository = creditRepository;
        this.accountService = accountService;
        this.clientService = clientService ;
    }


    public  boolean requestCredit(Client client, BigDecimal montant, int dureeMois, BigDecimal tauxInteret, InterestType interestType){

        List<Account> accounts = accountService.getAccountsByClient(client.getId());
      Account creditAccount = accounts.stream().filter(a->a.getType()== AccountType.Credit).findFirst().orElse(null);

      if(creditAccount == null){

          System.out.println("Client must have a CREDIT account to request a credit.");

          return false;
      }

        List<Credit> existingCredits = creditRepository.findByAccountId(creditAccount.getId());
        boolean hasLateCredit = existingCredits.stream()
                .anyMatch(c -> c.getStatus() == CreditStatus.LATE);

        if (hasLateCredit) {
            System.out.println("Credit denied: client already has a credit with status LATE.");
            return false;
        }
        BigDecimal mensualite;
        if (interestType == InterestType.SIMPLE) {
            mensualite = montant.add(montant.multiply(tauxInteret).divide(BigDecimal.valueOf(100)))
                    .divide(BigDecimal.valueOf(dureeMois), 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal monthlyRate = tauxInteret.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);
            mensualite = montant.multiply(monthlyRate)
                    .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyRate).pow(-dureeMois)), 2, RoundingMode.HALF_UP);
        }

        Credit credit = new Credit();
        credit.setMontant(montant);
        credit.setDureeMois(dureeMois);
        credit.setTauxInteret(tauxInteret);
        credit.setTypeInteret(interestType);
        credit.setStartDate(LocalDate.now());
        credit.setEndDate(LocalDate.now().plusMonths(dureeMois));
        credit.setMonthlyPayment(mensualite);
        credit.setAccountId(creditAccount.getId());

        if(mensualite.compareTo(client.getSalaire().multiply(BigDecimal.valueOf(0.4))) > 0){

            credit.setStatus(CreditStatus.REJECTED);
            creditRepository.save(credit);
            System.out.println(" Credit denied: monthly payment exceeds 40% of salary.");
            return false;
        }


        credit.setStatus(CreditStatus.PENDING);
        creditRepository.save(credit);
        return true;
    }


    public void depositMonthlySalary() {
        List<Client> clients = clientService.getAllClients();
        for (Client client : clients) {
            List<Account> accounts = accountService.getAccountsByClient(client.getId());
            Account currentAccount = accounts.stream()
                    .filter(a -> a.getType() == AccountType.Courant)
                    .findFirst()
                    .orElse(null);
            if (currentAccount != null) {
                currentAccount.setBalance(currentAccount.getBalance().add(client.getSalaire()));
                accountService.updateAccount(currentAccount);
                System.out.println("Salary added to client " + client.getNom() + " current account.");
            }
        }
    }

    public void payMonthlyCredit() {
        List<Client> clients = clientService.getAllClients();
        for (Client client : clients) {
            List<Credit> credits = creditRepository.findByStatus(CreditStatus.APPROVED);
            for (Credit credit : credits) {
                List<Account> accounts = accountService.getAccountsByClient(client.getId());
                Account currentAccount = accounts.stream()
                        .filter(a -> a.getType() == AccountType.Courant)
                        .findFirst()
                        .orElse(null);
                Account creditAccount = accounts.stream()
                        .filter(a -> a.getType() == AccountType.Credit)
                        .findFirst()
                        .orElse(null);

                if (currentAccount != null && creditAccount != null) {
                    if (currentAccount.getBalance().compareTo(credit.getMonthlyPayment()) >= 0) {
                        currentAccount.setBalance(currentAccount.getBalance().subtract(credit.getMonthlyPayment()));
                        creditAccount.setBalance(creditAccount.getBalance().add(credit.getMonthlyPayment()));
                        accountService.updateAccount(currentAccount);
                        accountService.updateAccount(creditAccount);

                        System.out.println("Monthly payment of " + credit.getMonthlyPayment() +
                                " deducted from client " + client.getNom() +
                                " current account and added to credit account.");
                    } else {
                        System.out.println("Insufficient balance for client " + client.getNom() +
                                " to pay monthly credit of " + credit.getMonthlyPayment());
                    }
                }
            }
        }
    }
    @Override
    public Optional<Credit> getCreditById(UUID id) {
        return creditRepository.findById(id);
    }

    @Override
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    @Override
    public List<Credit> getCreditsByStatus(CreditStatus status) {
        return creditRepository.findByStatus(status);
    }

    @Override
    public void processCreditApproval(Credit credit, boolean approve) {
        if (credit.getStatus() != CreditStatus.PENDING) {
            System.out.println("This credit request is already processed.");
            return;
        }
        if (approve) {
            credit.setStatus(CreditStatus.APPROVED);
            creditRepository.updateStatus(credit.getId(), CreditStatus.APPROVED);
            System.out.println("Credit approved!");
        } else {
            credit.setStatus(CreditStatus.REJECTED);
            creditRepository.updateStatus(credit.getId(), CreditStatus.REJECTED);
            System.out.println("Credit rejected.");
        }
    }


}
