package com.bank.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testCreateAccount() {
        Account account = new Account("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        assertEquals("ACC-001", account.getAccountId());
        assertEquals("CUST-001", account.getCustomerId());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void testCreateAccountWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Account(null, "CUST-001", new BigDecimal("1000.00"));
        });
    }

    @Test
    void testCreateAccountWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Account("ACC-001", "CUST-001", new BigDecimal("-100.00"));
        });
    }

    @Test
    void testCreditAccount() {
        Account account = new Account("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        account.credit(new BigDecimal("250.00"));
        
        assertEquals(new BigDecimal("1250.00"), account.getBalance());
    }

    @Test
    void testDebitAccount() {
        Account account = new Account("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        account.debit(new BigDecimal("250.00"));
        
        assertEquals(new BigDecimal("750.00"), account.getBalance());
    }

    @Test
    void testDebitAccountInsufficientFunds() {
        Account account = new Account("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        
        assertThrows(IllegalStateException.class, () -> {
            account.debit(new BigDecimal("1500.00"));
        });
    }

    @Test
    void testAccountEquality() {
        Account account1 = new Account("ACC-001", "CUST-001", new BigDecimal("1000.00"));
        Account account2 = new Account("ACC-001", "CUST-002", new BigDecimal("500.00"));
        Account account3 = new Account("ACC-002", "CUST-001", new BigDecimal("1000.00"));
        
        assertEquals(account1, account2);
        assertNotEquals(account1, account3);
    }
}
