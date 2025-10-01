package org.example.service;

import org.example.entities.Account;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Account createAccount( AccountType type,  BigDecimal balance,Currency currency,UUID clientId);
    List<Account> getAccountsByClient(UUID clientId);
    void closeAccount(UUID accountId);

    void freezeAccount(UUID accountId);

    void reopenAccount(UUID accountId);

    Optional<Account> getAccount(UUID accountId);
}
