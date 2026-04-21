package com.cryptotrading.service;

import com.cryptotrading.models.Wallet;
import com.cryptotrading.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WalletService Tests")
class WalletServiceTest {

    private WalletRepository walletRepo;
    private WalletService walletService;
    private UUID userId;

    @BeforeEach
    void setUp() {
        walletRepo = new WalletRepository();
        walletService = new WalletService(walletRepo);
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("createWallet creates a wallet with zero balance and correct ticker")
    void createWalletZeroBalance() {
        Wallet wallet = walletService.createWallet(userId, "BTC");

        assertNotNull(wallet);
        assertNotNull(wallet.getId());
        assertEquals(userId, wallet.getUserId());
        assertEquals("BTC", wallet.getAssetTicker());
        assertEquals(0, BigDecimal.ZERO.compareTo(wallet.getBalance()));
    }

    @Test
    @DisplayName("createWallet throws if user already has a wallet for same asset")
    void createWalletDuplicateThrows() {
        walletService.createWallet(userId, "BTC");

        assertThrows(RuntimeException.class, () -> walletService.createWallet(userId, "BTC"));
    }

    @Test
    @DisplayName("createWallet allows different tickers for same user")
    void createWalletDifferentTickersAllowed() {
        walletService.createWallet(userId, "BTC");
        Wallet ethWallet = walletService.createWallet(userId, "ETH");

        assertNotNull(ethWallet);
        assertEquals("ETH", ethWallet.getAssetTicker());
    }

    @Test
    @DisplayName("deposit increases wallet balance")
    void depositIncreasesBalance() {
        Wallet wallet = walletService.createWallet(userId, "USD");
        walletService.deposit(wallet.getId(), new BigDecimal("500"));

        Wallet updated = walletRepo.findById(wallet.getId());
        assertEquals(0, new BigDecimal("500").compareTo(updated.getBalance()));
    }

    @Test
    @DisplayName("deposit throws for nonexistent wallet")
    void depositThrowsForUnknownWallet() {
        assertThrows(RuntimeException.class,
                () -> walletService.deposit(UUID.randomUUID(), new BigDecimal("100")));
    }

    @Test
    @DisplayName("deposit throws for negative amount")
    void depositThrowsForNegativeAmount() {
        Wallet wallet = walletService.createWallet(userId, "BTC");

        assertThrows(IllegalArgumentException.class,
                () -> walletService.deposit(wallet.getId(), new BigDecimal("-100")));
    }

    @Test
    @DisplayName("deposit throws for zero amount")
    void depositThrowsForZeroAmount() {
        Wallet wallet = walletService.createWallet(userId, "BTC");

        assertThrows(IllegalArgumentException.class,
                () -> walletService.deposit(wallet.getId(), BigDecimal.ZERO));
    }

    @Test
    @DisplayName("deposit throws for null amount")
    void depositThrowsForNullAmount() {
        Wallet wallet = walletService.createWallet(userId, "BTC");

        assertThrows(IllegalArgumentException.class,
                () -> walletService.deposit(wallet.getId(), null));
    }

    @Test
    @DisplayName("withdraw decreases wallet balance")
    void withdrawDecreasesBalance() {
        Wallet wallet = walletService.createWallet(userId, "USD");
        walletService.deposit(wallet.getId(), new BigDecimal("1000"));
        walletService.withdraw(wallet.getId(), new BigDecimal("300"));

        assertEquals(0, new BigDecimal("700").compareTo(wallet.getBalance()));
    }

    @Test
    @DisplayName("withdraw throws for insufficient balance")
    void withdrawThrowsForInsufficientBalance() {
        Wallet wallet = walletService.createWallet(userId, "USD");
        walletService.deposit(wallet.getId(), new BigDecimal("100"));

        assertThrows(RuntimeException.class,
                () -> walletService.withdraw(wallet.getId(), new BigDecimal("200")));
    }

    @Test
    @DisplayName("withdraw throws for negative amount")
    void withdrawThrowsForNegativeAmount() {
        Wallet wallet = walletService.createWallet(userId, "BTC");

        assertThrows(IllegalArgumentException.class,
                () -> walletService.withdraw(wallet.getId(), new BigDecimal("-50")));
    }

    @Test
    @DisplayName("getWalletById returns the correct wallet")
    void getWalletByIdReturnsWallet() {
        Wallet wallet = walletService.createWallet(userId, "BTC");

        Wallet found = walletService.getWalletById(wallet.getId());
        assertNotNull(found);
        assertEquals(wallet.getId(), found.getId());
    }

    @Test
    @DisplayName("getWalletById throws for unknown wallet")
    void getWalletByIdThrowsForUnknown() {
        assertThrows(RuntimeException.class,
                () -> walletService.getWalletById(UUID.randomUUID()));
    }
}
