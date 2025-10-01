package org.example.repository;

import org.example.entities.Account;
import org.example.entities.enums.AccountStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository  {
    void save(Account account);
    List<Account> findByClientId(UUID clientId);
    void updateStatus(UUID accountId, AccountStatus status);
    Optional<Account> findById(UUID accountId);
    void update(Account account);

}
