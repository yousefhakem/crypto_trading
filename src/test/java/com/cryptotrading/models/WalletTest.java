package com.cryptotrading.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Wallet Model Tests")
class WalletTest {

    private Wallet createWallet(String ticker) {
        return new Wallet(UUID.randomUUID(), UUID.randomUUID(), ticker);
    }

    @Test
    @DisplayName("Constructor sets all fields correctly with zero balance")
    void constructorSetsAllFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Wallet wallet = new Wallet(id, userId, "BTC");

        assertEquals(id, wallet.getId());
        assertEquals(userId, wallet.getUserId());
        assertEquals("BTC", wallet.getAssetTicker());
        assertEquals(0, BigDecimal.ZERO.compareTo(wallet.getBalance()),
                "New wallet should start with zero balance");
    }

    @Test
    @DisplayName("add() increases balance correctly")
    void addIncreasesBalance() {
        Wallet wallet = createWallet("ETH");
        wallet.add(new BigDecimal("50.25"));

        assertEquals(0, new BigDecimal("50.25").compareTo(wallet.getBalance()));
    }

    @Test
    @DisplayName("sub() decreases balance correctly")
    void subDecreasesBalance() {
        Wallet wallet = createWallet("BTC");
        wallet.add(new BigDecimal("100"));
        wallet.sub(new BigDecimal("30"));

        assertEquals(0, new BigDecimal("70").compareTo(wallet.getBalance()));
    }

    @Test
    @DisplayName("add() and sub() chain correctly")
    void addAndSubChain() {
        Wallet wallet = createWallet("USD");
        wallet.add(new BigDecimal("1000"));
        wallet.sub(new BigDecimal("250"));
        wallet.add(new BigDecimal("100"));

        assertEquals(0, new BigDecimal("850").compareTo(wallet.getBalance()));
    }

    @Test
    @DisplayName("BigDecimal precision is preserved")
    void precisionPreserved() {
        Wallet wallet = createWallet("USDT");
        wallet.add(new BigDecimal("0.1"));
        wallet.add(new BigDecimal("0.2"));

        // BigDecimal avoids the classic 0.1 + 0.2 != 0.3 floating point bug
        assertEquals(0, new BigDecimal("0.3").compareTo(wallet.getBalance()));
    }

    @Test
    @DisplayName("getAssetTicker returns the assigned ticker")
    void getAssetTickerReturnsCorrectValue() {
        Wallet wallet = createWallet("ETH");
        assertEquals("ETH", wallet.getAssetTicker());
    }
}
