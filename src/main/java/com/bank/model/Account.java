package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bank account
 */
public class Account {
    private final String accountId;
    private final String customerId;
    private BigDecimal balance;
    private final LocalDateTime createdAt;
    private AccountStatus status;

    public Account(String accountId, String customerId, BigDecimal initialBalance) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be null or negative");
        }
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.createdAt = LocalDateTime.now();
        this.status = AccountStatus.ACTIVE;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public synchronized void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public synchronized void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}
