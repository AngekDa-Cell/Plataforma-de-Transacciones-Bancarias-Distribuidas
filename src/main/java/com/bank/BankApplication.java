package com.bank;

import com.bank.model.*;
import com.bank.service.BankingService;
import com.bank.util.IdGenerator;

import java.math.BigDecimal;

/**
 * Main application class for the Distributed Banking Transaction Platform
 */
public class BankApplication {

    public static void main(String[] args) {
        System.out.println("=== Distributed Banking Transaction Platform ===\n");

        BankingService bankingService = new BankingService();

        // Create customers
        System.out.println("Creating customers...");
        Customer customer1 = bankingService.createCustomer(
            IdGenerator.generateCustomerId(),
            "John Doe",
            "john.doe@example.com",
            "+1-555-0001"
        );
        System.out.println("Created: " + customer1);

        Customer customer2 = bankingService.createCustomer(
            IdGenerator.generateCustomerId(),
            "Jane Smith",
            "jane.smith@example.com",
            "+1-555-0002"
        );
        System.out.println("Created: " + customer2);

        // Create accounts
        System.out.println("\nCreating accounts...");
        Account account1 = bankingService.createAccount(
            IdGenerator.generateAccountId(),
            customer1.getCustomerId(),
            new BigDecimal("1000.00")
        );
        System.out.println("Created: " + account1);

        Account account2 = bankingService.createAccount(
            IdGenerator.generateAccountId(),
            customer2.getCustomerId(),
            new BigDecimal("500.00")
        );
        System.out.println("Created: " + account2);

        // Perform deposit
        System.out.println("\nPerforming deposit...");
        Transaction deposit = bankingService.deposit(account1.getAccountId(), new BigDecimal("250.00"));
        System.out.println("Deposit completed: " + deposit);
        System.out.println("Account 1 balance: $" + bankingService.getBalance(account1.getAccountId()));

        // Perform withdrawal
        System.out.println("\nPerforming withdrawal...");
        Transaction withdrawal = bankingService.withdraw(account2.getAccountId(), new BigDecimal("100.00"));
        System.out.println("Withdrawal completed: " + withdrawal);
        System.out.println("Account 2 balance: $" + bankingService.getBalance(account2.getAccountId()));

        // Perform transfer
        System.out.println("\nPerforming transfer...");
        Transaction transfer = bankingService.transfer(
            account1.getAccountId(),
            account2.getAccountId(),
            new BigDecimal("300.00")
        );
        System.out.println("Transfer completed: " + transfer);
        System.out.println("Account 1 balance: $" + bankingService.getBalance(account1.getAccountId()));
        System.out.println("Account 2 balance: $" + bankingService.getBalance(account2.getAccountId()));

        // Display transaction history
        System.out.println("\nTransaction history for Account 1:");
        bankingService.getAccountTransactions(account1.getAccountId())
            .forEach(txn -> System.out.println("  " + txn));

        System.out.println("\nTransaction history for Account 2:");
        bankingService.getAccountTransactions(account2.getAccountId())
            .forEach(txn -> System.out.println("  " + txn));

        System.out.println("\n=== Banking operations completed successfully ===");
    }
}
