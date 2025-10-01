package org.example.service.implementation;

import org.example.entities.Account;
import org.example.entities.enums.AccountStatus;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.Currency;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountServiceImp implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


  public  Account createAccount(AccountType type, BigDecimal balance, Currency currency, UUID clientId){
        List<Account> existingAccounts = accountRepository.findByClientId(clientId);
      if (existingAccounts.size() >= 3) {
          System.out.println(" Client already has 3 accounts.");
          return null;
      }


      boolean typeExists = existingAccounts.stream()
              .anyMatch(acc -> acc.getType().equals(type));
      if (typeExists) {
          System.out.println(" Client already has an account of type " + type);
          return null;
      }

      Account account = new Account(type, balance, currency, clientId);
        accountRepository.save(account);
        return account;

    }
  public  List<Account> getAccountsByClient(UUID clientId){
        return accountRepository.findByClientId(clientId);

  }


    public void closeAccount(UUID accountId) {
        accountRepository.updateStatus(accountId, AccountStatus.CLOSED);
    }

    @Override
    public void freezeAccount(UUID accountId) {
        accountRepository.updateStatus(accountId, AccountStatus.FROZEN);
    }

    @Override
    public void reopenAccount(UUID accountId) {
        accountRepository.updateStatus(accountId, AccountStatus.OPEN);
    }

    public Optional<Account> getAccount(UUID accountId) {
        return accountRepository.findById(accountId);
    }




}
