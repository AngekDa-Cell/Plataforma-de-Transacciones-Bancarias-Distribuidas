package com.bank.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs
 */
public class IdGenerator {
    private static final AtomicLong customerCounter = new AtomicLong(0);
    private static final AtomicLong accountCounter = new AtomicLong(0);
    private static final AtomicLong transactionCounter = new AtomicLong(0);

    /**
     * Generate a unique UUID-based ID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a sequential customer ID
     */
    public static String generateCustomerId() {
        return "CUST-" + String.format("%08d", customerCounter.incrementAndGet());
    }

    /**
     * Generate a sequential account ID
     */
    public static String generateAccountId() {
        return "ACC-" + String.format("%010d", accountCounter.incrementAndGet());
    }

    /**
     * Generate a sequential transaction ID
     */
    public static String generateTransactionId() {
        return "TXN-" + String.format("%012d", transactionCounter.incrementAndGet());
    }
}
