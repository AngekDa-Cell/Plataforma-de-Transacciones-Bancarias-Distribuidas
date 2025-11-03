package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a bank transaction
 */
public class Transaction {
    private final String transactionId;
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final BigDecimal amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;
    private TransactionStatus status;
    private String description;

    public Transaction(String transactionId, String sourceAccountId, 
                      String destinationAccountId, BigDecimal amount, 
                      TransactionType type) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", sourceAccountId='" + sourceAccountId + '\'' +
                ", destinationAccountId='" + destinationAccountId + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
