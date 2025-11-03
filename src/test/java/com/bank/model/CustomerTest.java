package com.bank.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        
        assertEquals("CUST-001", customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals("+1-555-0001", customer.getPhone());
    }

    @Test
    void testCreateCustomerWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(null, "John Doe", "john@example.com", "+1-555-0001");
        });
    }

    @Test
    void testCreateCustomerWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("CUST-001", null, "john@example.com", "+1-555-0001");
        });
    }

    @Test
    void testCustomerEquality() {
        Customer customer1 = new Customer("CUST-001", "John Doe", "john@example.com", "+1-555-0001");
        Customer customer2 = new Customer("CUST-001", "Jane Doe", "jane@example.com", "+1-555-0002");
        Customer customer3 = new Customer("CUST-002", "John Doe", "john@example.com", "+1-555-0001");
        
        assertEquals(customer1, customer2);
        assertNotEquals(customer1, customer3);
    }
}
