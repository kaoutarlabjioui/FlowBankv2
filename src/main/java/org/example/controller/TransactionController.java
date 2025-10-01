package org.example.controller;

import org.example.entities.Account;
import org.example.entities.Client;
import org.example.entities.FeeRule;
import org.example.entities.Transaction;
import org.example.entities.enums.FeeMode;
import org.example.entities.enums.TransactionStatus;
import org.example.entities.enums.TransactionType;
import org.example.service.AccountService;
import org.example.service.ClientService;
import org.example.service.TransactionService;
import org.example.utils.ConsoleUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransactionController {

    private TransactionService transactionService;
    private AccountService accountService;
    private ClientService clientService;

    public TransactionController(TransactionService transactionService,
                                 AccountService accountService,
                                 ClientService clientService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.clientService = clientService;
    }


    public void deposit() {
        System.out.println("\n=== DEPOSIT ===");
        Client client = selectClient();
        if (client == null) return;

        Account account = selectAccount(client);
        if (account == null) return;

        BigDecimal amount = ConsoleUtils.readPositiveBigDecimal("Enter deposit amount: ");

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setMontant(amount);
        transaction.setCompteDestination(account.getId());
        transaction.setDevise(account.getDevise());

        transactionService.createTransaction(transaction);
        System.out.println(" Deposit successful. New balance: " + account.getBalance());
    }
    public void withdraw() {
        System.out.println("\n=== WITHDRAW ===");
        Client client = selectClient();
        if (client == null) return;

        Account account = selectAccount(client);
        if (account == null) return;

        BigDecimal amount = ConsoleUtils.readPositiveBigDecimal("Enter withdrawal amount: ");

        if (account.getBalance().compareTo(amount) < 0) {
            System.out.println(" Insufficient balance.");
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setMontant(amount);
        transaction.setCompteSource(account.getId());
        transaction.setDevise(account.getDevise());

        transactionService.createTransaction(transaction);
        System.out.println(" Withdrawal successful. New balance: " + account.getBalance());
    }



    public void transfer() {
        System.out.println("\n=== TRANSFER ===");

        Client client = selectClient();
        if (client == null) return;

        Account source = selectAccount(client);
        if (source == null) return;

        BigDecimal amount = ConsoleUtils.readPositiveBigDecimal("Enter transfer amount: ");
        if (source.getBalance().compareTo(amount) < 0) {
            System.out.println(" Insufficient balance.");
            return;
        }

        String choice = ConsoleUtils.readString("Transfer to (1) own account / (2) another client / (3) external: ");

        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setMontant(amount);
        transaction.setCompteSource(source.getId());
        transaction.setDevise(source.getDevise());

        switch (choice) {
            case "1" -> {
                Account destination = selectAccount(client, source);
                if (destination == null) return;
                transaction.setCompteDestination(destination.getId());
            }
            case "2" -> {
                Client otherClient = selectClient();
                if (otherClient == null) return;
                Account destination = selectAccount(otherClient);
                if (destination == null) return;
                transaction.setCompteDestination(destination.getId());
            }
            case "3" -> {
                String rib = ConsoleUtils.readString("Enter external RIB: ");
                transactionService.transferToExternalAccount(source, amount, rib);
                return;
            }
            default -> {
                System.out.println(" Invalid choice.");
                return;
            }
        }

        // Appel au service pour exécuter la transaction
        transactionService.createTransaction(transaction);
        System.out.println("✅ Transaction executed successfully!");
    }








    private Client selectClient() {
        String cin = ConsoleUtils.readString("Enter client's CIN: ");
        Client client = clientService.getClientByCin(cin);
        if (client == null) {
            System.out.println("❌ Client not found.");
        }
        return client;
    }

    private Account selectAccount(Client client) {
        return selectAccount(client, null);
    }

    private Account selectAccount(Client client, Account excludeAccount) {
        List<Account> accounts = accountService.getAccountsByClient(client.getId());
        if (accounts.isEmpty()) {
            System.out.println("❌ This client has no accounts.");
            return null;
        }

        System.out.println("Select an account:");
        int index = 1;
        for (Account acc : accounts) {
            if (excludeAccount != null && acc.getId().equals(excludeAccount.getId())) continue;
            System.out.println(index + ". " + acc.getType() + " | Balance: " + acc.getBalance() + " | Currency: " + acc.getDevise());
            index++;
        }

        int choice = ConsoleUtils.readInt("Choose account by number: ", 1, accounts.size());
        return accounts.get(choice - 1);
    }

}