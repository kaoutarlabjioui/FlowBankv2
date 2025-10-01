package org.example.controller;

import org.example.entities.Account;
import org.example.entities.Client;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.Currency;
import org.example.service.AccountService;
import org.example.service.ClientService;
import org.example.utils.ConsoleUtils;

import java.math.BigDecimal;

public class AccountController {

    private AccountService accountservice;
    private ClientService clientService;

    public AccountController(AccountService accountservice , ClientService clientService){
        this.accountservice = accountservice;
        this.clientService = clientService;
    }

    public void createAccount(){
        System.out.println("\n=== CREATE ACCOUNT ===");
    String cin = ConsoleUtils.readString("Enter the CIN of the client : ");

    Client selectedClient = clientService.getClientByCin(cin);

    if(selectedClient == null){
        System.out.println("No client found with this cin : " +cin);
        return;
    }else {
        System.out.println("📋 client: " + selectedClient.getNom()+ " " + selectedClient.getPrenom() + " (CIN: " + selectedClient.getCin()+ ")");
    }
        AccountType[] types = AccountType.values();
        System.out.println("Available account types : ");

        for(int i = 0 ; i< types.length;i++){
            System.out.println((i + 1) + " . " + types[i]);
        }

        int typeChoice = ConsoleUtils.readInt("Choose account type by number : " ,1, types.length);
        AccountType type = types[typeChoice - 1];

        Currency[] currencies = Currency.values();
        System.out.println("available currencies : ");

        for(int i=0 ; i< currencies.length;i++){
            System.out.println((i + 1) + " . " + currencies[i]);
        }
        int currencyChoice = ConsoleUtils.readInt("Choose currency by number : ",1,currencies.length);
        Currency currency = currencies[currencyChoice - 1];


        BigDecimal balance = ConsoleUtils.readPositiveBigDecimal("Enter initial balance : ");
        Account account = accountservice.createAccount(type,balance,currency,selectedClient.getId());

    }











}
