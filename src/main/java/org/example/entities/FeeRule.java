package org.example.entities;

import org.example.entities.enums.Currency;
import org.example.entities.enums.FeeMode;
import org.example.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class FeeRule {



        private UUID id;
        private TransactionType operationType;
        private FeeMode mode;
        private BigDecimal value;
        private Currency currency;
        private boolean isActive;
        private LocalDateTime createdAt;

        public FeeRule() {}

        public FeeRule(TransactionType operationType, FeeMode mode, BigDecimal value, Currency currency) {
            this.id = UUID.randomUUID();
            this.operationType = operationType;
            this.mode = mode;
            this.value = value;
            this.currency = currency;
            this.isActive = true;
            this.createdAt = LocalDateTime.now();
        }


        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public TransactionType getOperationType() { return operationType; }
        public void setOperationType(TransactionType operationType) { this.operationType = operationType; }
        public FeeMode getMode() { return mode; }
        public void setMode(FeeMode mode) { this.mode = mode; }
        public BigDecimal getValue() { return value; }
        public void setValue(BigDecimal value) { this.value = value; }
        public Currency getCurrency() { return currency; }
        public void setCurrency(Currency currency) { this.currency = currency; }
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

