package com.bank.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs
 */
public class IdGenerator {
    private static final AtomicLong counter = new AtomicLong(0);

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
        return "CUST-" + String.format("%08d", counter.incrementAndGet());
    }

    /**
     * Generate a sequential account ID
     */
    public static String generateAccountId() {
        return "ACC-" + String.format("%010d", counter.incrementAndGet());
    }

    /**
     * Generate a sequential transaction ID
     */
    public static String generateTransactionId() {
        return "TXN-" + String.format("%012d", counter.incrementAndGet());
    }
}
