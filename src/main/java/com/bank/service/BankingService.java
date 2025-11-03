package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.TransactionException;
import com.bank.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service class for banking operations with distributed transaction support
 */
public class BankingService {
    private final Map<String, Account> accounts;
    private final Map<String, Customer> customers;
    private final Map<String, Transaction> transactions;
    private final Map<String, Lock> accountLocks;

    public BankingService() {
        this.accounts = new ConcurrentHashMap<>();
        this.customers = new ConcurrentHashMap<>();
        this.transactions = new ConcurrentHashMap<>();
        this.accountLocks = new ConcurrentHashMap<>();
    }

    /**
     * Create a new customer
     */
    public Customer createCustomer(String customerId, String name, String email, String phone) {
        Customer customer = new Customer(customerId, name, email, phone);
        customers.put(customerId, customer);
        return customer;
    }

    /**
     * Get customer by ID
     */
    public Customer getCustomer(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new AccountNotFoundException("Customer not found: " + customerId);
        }
        return customer;
    }

    /**
     * Create a new account
     */
    public Account createAccount(String accountId, String customerId, BigDecimal initialBalance) {
        // Verify customer exists
        if (!customers.containsKey(customerId)) {
            throw new AccountNotFoundException("Customer not found: " + customerId);
        }
        
        Account account = new Account(accountId, customerId, initialBalance);
        accounts.put(accountId, account);
        accountLocks.putIfAbsent(accountId, new ReentrantLock());
        return account;
    }

    /**
     * Get account by ID
     */
    public Account getAccount(String accountId) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }
        return account;
    }

    /**
     * Get account balance
     */
    public BigDecimal getBalance(String accountId) {
        return getAccount(accountId).getBalance();
    }

    /**
     * Deposit money into an account
     */
    public Transaction deposit(String accountId, BigDecimal amount) {
        Lock lock = accountLocks.get(accountId);
        if (lock == null) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }

        lock.lock();
        try {
            Account account = getAccount(accountId);
            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new TransactionException("Account is not active: " + accountId);
            }

            String transactionId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction(transactionId, null, accountId, 
                                                     amount, TransactionType.DEPOSIT);
            
            account.credit(amount);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setDescription("Deposit to account " + accountId);
            transactions.put(transactionId, transaction);
            
            return transaction;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Withdraw money from an account
     */
    public Transaction withdraw(String accountId, BigDecimal amount) {
        Lock lock = accountLocks.get(accountId);
        if (lock == null) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }

        lock.lock();
        try {
            Account account = getAccount(accountId);
            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new TransactionException("Account is not active: " + accountId);
            }

            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in account: " + accountId);
            }

            String transactionId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction(transactionId, accountId, null, 
                                                     amount, TransactionType.WITHDRAWAL);
            
            account.debit(amount);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setDescription("Withdrawal from account " + accountId);
            transactions.put(transactionId, transaction);
            
            return transaction;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Transfer money between accounts with distributed transaction support
     */
    public Transaction transfer(String sourceAccountId, String destinationAccountId, BigDecimal amount) {
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new TransactionException("Cannot transfer to the same account");
        }

        // Get locks for both accounts in consistent order to prevent deadlock
        String firstLockId = sourceAccountId.compareTo(destinationAccountId) < 0 ? 
                           sourceAccountId : destinationAccountId;
        String secondLockId = sourceAccountId.compareTo(destinationAccountId) < 0 ? 
                            destinationAccountId : sourceAccountId;

        Lock firstLock = accountLocks.get(firstLockId);
        Lock secondLock = accountLocks.get(secondLockId);

        if (firstLock == null || secondLock == null) {
            throw new AccountNotFoundException("One or both accounts not found");
        }

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                Account sourceAccount = getAccount(sourceAccountId);
                Account destinationAccount = getAccount(destinationAccountId);

                if (sourceAccount.getStatus() != AccountStatus.ACTIVE) {
                    throw new TransactionException("Source account is not active: " + sourceAccountId);
                }
                if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
                    throw new TransactionException("Destination account is not active: " + destinationAccountId);
                }

                if (sourceAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Insufficient funds in source account: " + sourceAccountId);
                }

                String transactionId = UUID.randomUUID().toString();
                Transaction transaction = new Transaction(transactionId, sourceAccountId, 
                                                         destinationAccountId, amount, 
                                                         TransactionType.TRANSFER);

                // Execute transfer
                sourceAccount.debit(amount);
                destinationAccount.credit(amount);
                
                transaction.setStatus(TransactionStatus.COMPLETED);
                transaction.setDescription("Transfer from " + sourceAccountId + " to " + destinationAccountId);
                transactions.put(transactionId, transaction);
                
                return transaction;
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    /**
     * Get transaction by ID
     */
    public Transaction getTransaction(String transactionId) {
        Transaction transaction = transactions.get(transactionId);
        if (transaction == null) {
            throw new TransactionException("Transaction not found: " + transactionId);
        }
        return transaction;
    }

    /**
     * Get all transactions for an account
     */
    public List<Transaction> getAccountTransactions(String accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            if (accountId.equals(transaction.getSourceAccountId()) || 
                accountId.equals(transaction.getDestinationAccountId())) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    /**
     * Get all accounts for a customer
     */
    public List<Account> getCustomerAccounts(String customerId) {
        List<Account> customerAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (customerId.equals(account.getCustomerId())) {
                customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }

    /**
     * Close an account
     */
    public void closeAccount(String accountId) {
        Lock lock = accountLocks.get(accountId);
        if (lock == null) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }

        lock.lock();
        try {
            Account account = getAccount(accountId);
            if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new TransactionException("Cannot close account with non-zero balance");
            }
            account.setStatus(AccountStatus.CLOSED);
        } finally {
            lock.unlock();
        }
    }
}
