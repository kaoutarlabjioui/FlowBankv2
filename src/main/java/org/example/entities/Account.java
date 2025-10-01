package org.example.entities;
import org.example.entities.enums.AccountStatus;
import org.example.entities.enums.AccountType;
import org.example.entities.enums.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {

    private UUID id;
    private AccountType type;
    private BigDecimal balance;
    private Currency devise;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime dateCloture;
    private UUID clientId;


    public Account() {}

    public Account( AccountType type, BigDecimal balance, Currency devise,  UUID clientId) {

        this.id = UUID.randomUUID();
        this.type = type;
        this.balance = balance.setScale(2, RoundingMode.HALF_EVEN);
        this.devise = devise;
        this.status = AccountStatus.OPEN; //  par défaut
        this.createdAt = LocalDateTime.now(); //  initialisé à la création
        this.dateCloture = null;
        this.clientId = clientId;

    }

    public Account(UUID id, AccountType type, BigDecimal balance, Currency devise,
                   AccountStatus status, LocalDateTime dateCloture, UUID clientId) {
        this.id = id;
        this.type = type;
        this.balance = balance.setScale(2, RoundingMode.HALF_EVEN);
        this.devise = devise;
        this.status = status;
        this.dateCloture = dateCloture;
        this.clientId = clientId;
    }

    public UUID getId() { return id; }
    public AccountType getType() { return type; }
    public BigDecimal getBalance() { return balance; }
    public Currency getDevise() { return devise; }
    public AccountStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDateCloture() { return dateCloture; }
    public UUID getClientId() { return clientId; }

    public void setId(UUID id) { this.id = id; }
    public void setType(AccountType type) { this.type = type; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setDevise(Currency devise) { this.devise = devise; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setDateCloture(LocalDateTime dateCloture) { this.dateCloture = dateCloture; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public  void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
    public void transfer(BigDecimal amount, Account destination) {
        this.balance = this.balance.subtract(amount);
        destination.deposit(amount);
    }
    public void transfer(BigDecimal amount, String RIB) {
        this.balance = this.balance.subtract(amount);
    }
    @Override
    public String toString() {

        return "Account{" + "id=" + id + ", type=" + type + ", balance=" + balance + ", devise=" + devise + ", status=" + status + ", createdAt=" + createdAt + ", dateCloture=" + dateCloture + ", clientId=" + clientId + '}';
    }
}
