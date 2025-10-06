package org.example.jobs;

import org.example.entities.Client;
import org.example.entities.enums.AccountType;
import org.example.service.AccountService;
import org.example.service.ClientService;

import java.util.List;

public class SalaryJob implements Runnable {
    private final ClientService clientService;
    private final AccountService accountService;

    public SalaryJob(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    public void run(){
        System.out.println("=== Running Salary Job ===");

        List<Client> clients = clientService.getAllClients();

        for (Client client : clients ){
            accountService.getAccountsByClient(client.getId()).stream()
                    .filter(a->a.getType() == AccountType.Courant).findFirst()
                    .ifPresent(courant->{
                        courant.setBalance(courant.getBalance().add(client.getSalaire()));
                        accountService.updateAccount(courant);

                        System.out.println("salary added for "+ client.getNom()+" "+client.getPrenom());
                    });


        }

    }




}
