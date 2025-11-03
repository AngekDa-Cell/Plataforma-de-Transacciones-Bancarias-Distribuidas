package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.TransactionException;
import com.bank.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankingServiceTest {

    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        bankingService = new BankingService();
    }

    @Test
    void testCreateCustomer() {
        Customer customer = bankingService.createCustomer("CUST-001", "John Doe", 
                                                          "john@example.com", "+1-555-0001");
        
        assertNotNull(customer);
        assertEquals("CUST-001", customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
    }

    @Test
    void testGetCustomer() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        Customer customer = bankingService.getCustomer("CUST-001");
        
        assertNotNull(customer);
        assertEquals("CUST-001", customer.getCustomerId());
    }

    @Test
    void testGetNonExistentCustomer() {
        assertThrows(AccountNotFoundException.class, () -> {
            bankingService.getCustomer("CUST-999");
        });
    }

    @Test
    void testCreateAccount() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        Account account = bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        assertNotNull(account);
        assertEquals("ACC-001", account.getAccountId());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    void testCreateAccountForNonExistentCustomer() {
        assertThrows(AccountNotFoundException.class, () -> {
            bankingService.createAccount("ACC-001", "CUST-999", new BigDecimal("1000.00"));
        });
    }

    @Test
    void testDeposit() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        Transaction transaction = bankingService.deposit("ACC-001", new BigDecimal("250.00"));
        
        assertNotNull(transaction);
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(new BigDecimal("1250.00"), bankingService.getBalance("ACC-001"));
    }

    @Test
    void testWithdraw() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        Transaction transaction = bankingService.withdraw("ACC-001", new BigDecimal("250.00"));
        
        assertNotNull(transaction);
        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(new BigDecimal("750.00"), bankingService.getBalance("ACC-001"));
    }

    @Test
    void testWithdrawInsufficientFunds() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        assertThrows(InsufficientFundsException.class, () -> {
            bankingService.withdraw("ACC-001", new BigDecimal("1500.00"));
        });
    }

    @Test
    void testTransfer() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createCustomer("CUST-002", "Jane Smith", "jane@example.com", "+1-555-0002");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        bankingService.createAccount("ACC-002", "CUST-002", new BigDecimal("500.00"));
        
        Transaction transaction = bankingService.transfer("ACC-001", "ACC-002", new BigDecimal("300.00"));
        
        assertNotNull(transaction);
        assertEquals(TransactionType.TRANSFER, transaction.getType());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(new BigDecimal("700.00"), bankingService.getBalance("ACC-001"));
        assertEquals(new BigDecimal("800.00"), bankingService.getBalance("ACC-002"));
    }

    @Test
    void testTransferInsufficientFunds() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createCustomer("CUST-002", "Jane Smith", "jane@example.com", "+1-555-0002");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        bankingService.createAccount("ACC-002", "CUST-002", new BigDecimal("500.00"));
        
        assertThrows(InsufficientFundsException.class, () -> {
            bankingService.transfer("ACC-001", "ACC-002", new BigDecimal("1500.00"));
        });
    }

    @Test
    void testTransferToSameAccount() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        assertThrows(TransactionException.class, () -> {
            bankingService.transfer("ACC-001", "ACC-001", new BigDecimal("100.00"));
        });
    }

    @Test
    void testGetAccountTransactions() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        bankingService.deposit("ACC-001", new BigDecimal("250.00"));
        bankingService.withdraw("ACC-001", new BigDecimal("100.00"));
        
        List<Transaction> transactions = bankingService.getAccountTransactions("ACC-001");
        
        assertEquals(2, transactions.size());
    }

    @Test
    void testGetCustomerAccounts() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        bankingService.createAccount("ACC-002", "CUST-001", new BigDecimal("500.00"));
        
        List<Account> accounts = bankingService.getCustomerAccounts("CUST-001");
        
        assertEquals(2, accounts.size());
    }

    @Test
    void testCloseAccount() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", BigDecimal.ZERO);
        
        bankingService.closeAccount("ACC-001");
        
        Account account = bankingService.getAccount("ACC-001");
        assertEquals(AccountStatus.CLOSED, account.getStatus());
    }

    @Test
    void testCloseAccountWithBalance() {
        bankingService.createCustomer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        bankingService.createAccount("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        assertThrows(TransactionException.class, () -> {
            bankingService.closeAccount("ACC-001");
        });
    }
}
