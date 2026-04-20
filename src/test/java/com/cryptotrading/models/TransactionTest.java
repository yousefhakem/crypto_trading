package com.cryptotrading.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction Model Tests")
class TransactionTest {

    @Test
    @DisplayName("Constructor sets all fields and computes totalCost")
    void constructorComputesTotalCost() {
        UUID id = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("2.5");
        BigDecimal price = new BigDecimal("4000");

        Transaction tx = new Transaction(id, UUID.randomUUID(), UUID.randomUUID(),
                TransactionType.BUY, "BTC", amount, price);

        assertEquals(id, tx.getId());
        assertEquals(TransactionType.BUY, tx.getType());
        assertEquals("BTC", tx.getAssetTicker());
        assertEquals(0, new BigDecimal("10000").compareTo(tx.getTotalCost()),
                "totalCost should be amount * price (2.5 * 4000 = 10000)");
    }

    @Test
    @DisplayName("totalCost is computed with decimal precision")
    void totalCostDecimalPrecision() {
        BigDecimal amount = new BigDecimal("0.123");
        BigDecimal price = new BigDecimal("45678.90");

        Transaction tx = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                TransactionType.SELL, "ETH", amount, price);

        BigDecimal expected = new BigDecimal("5618.50470");
        assertEquals(0, expected.compareTo(tx.getTotalCost()));
    }

    @Test
    @DisplayName("All TransactionType enum values are accessible")
    void allTransactionTypesExist() {
        assertNotNull(TransactionType.BUY);
        assertNotNull(TransactionType.SELL);
        assertNotNull(TransactionType.DEPOSIT);
        assertNotNull(TransactionType.WITHDRAW);
        assertEquals(4, TransactionType.values().length);
    }

    @Test
    @DisplayName("assetTicker is stored correctly")
    void assetTickerIsStored() {
        Transaction tx = new Transaction(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                TransactionType.BUY, "USDT", new BigDecimal("1"), new BigDecimal("1"));
        assertEquals("USDT", tx.getAssetTicker());
    }
}
