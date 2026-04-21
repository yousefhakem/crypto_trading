package com.cryptotrading.service;

import com.cryptotrading.models.TransactionType;
import com.cryptotrading.repositories.TransactionRepository;
import com.cryptotrading.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TradeService Tests")
class TradeServiceTest {

    private WalletRepository walletRepo;
    private WalletService walletService;
    private TransactionService txService;
    private TradeService tradeService;
    private UUID userId;
    private UUID usdWalletId;
    private UUID btcWalletId;

    @BeforeEach
    void setUp() {
        walletRepo = new WalletRepository();
        walletService = new WalletService(walletRepo);
        txService = new TransactionService(new TransactionRepository());
        tradeService = new TradeService(txService, walletService);

        userId = UUID.randomUUID();

        // Create a USD wallet (quote) funded with $10,000
        var usdWallet = walletService.createWallet(userId, "USD");
        usdWalletId = usdWallet.getId();
        walletService.deposit(usdWalletId, new BigDecimal("10000"));

        // Create a BTC wallet (base) with 0 balance
        var btcWallet = walletService.createWallet(userId, "BTC");
        btcWalletId = btcWallet.getId();
    }

    @Test
    @DisplayName("buy() deducts totalCost from quote wallet and adds amount to base wallet")
    void buyDeductsFromQuoteAndAddsToBase() {
        // Buy 2 BTC @ $1000 each = $2000 total cost
        tradeService.buy(userId, usdWalletId, btcWalletId,
                new BigDecimal("2"), new BigDecimal("1000"));

        BigDecimal usdBalance = walletRepo.findById(usdWalletId).getBalance();
        BigDecimal btcBalance = walletRepo.findById(btcWalletId).getBalance();

        assertEquals(0, new BigDecimal("8000").compareTo(usdBalance),
                "USD: 10000 - (2 * 1000) = 8000");
        assertEquals(0, new BigDecimal("2").compareTo(btcBalance),
                "BTC: 0 + 2 = 2");
    }

    @Test
    @DisplayName("sell() adds totalRevenue to quote wallet and deducts amount from base wallet")
    void sellAddsToQuoteAndDeductsFromBase() {
        // First buy some BTC so we have something to sell
        walletService.deposit(btcWalletId, new BigDecimal("5"));

        // Sell 3 BTC @ $500 each = $1500 revenue
        tradeService.sell(userId, usdWalletId, btcWalletId,
                new BigDecimal("3"), new BigDecimal("500"));

        BigDecimal usdBalance = walletRepo.findById(usdWalletId).getBalance();
        BigDecimal btcBalance = walletRepo.findById(btcWalletId).getBalance();

        assertEquals(0, new BigDecimal("11500").compareTo(usdBalance),
                "USD: 10000 + (3 * 500) = 11500");
        assertEquals(0, new BigDecimal("2").compareTo(btcBalance),
                "BTC: 5 - 3 = 2");
    }

    @Test
    @DisplayName("buy() throws when insufficient quote balance")
    void buyThrowsWhenInsufficientBalance() {
        // Try to buy 100 BTC @ $1000 = $100,000 but only have $10,000
        assertThrows(RuntimeException.class,
                () -> tradeService.buy(userId, usdWalletId, btcWalletId,
                        new BigDecimal("100"), new BigDecimal("1000")));
    }

    @Test
    @DisplayName("sell() throws when insufficient base balance")
    void sellThrowsWhenInsufficientBalance() {
        // Try to sell 1 BTC but BTC wallet has 0
        assertThrows(RuntimeException.class,
                () -> tradeService.sell(userId, usdWalletId, btcWalletId,
                        new BigDecimal("1"), new BigDecimal("5000")));
    }

    @Test
    @DisplayName("buy() creates a BUY transaction record")
    void buyCreatesTransaction() {
        tradeService.buy(userId, usdWalletId, btcWalletId,
                new BigDecimal("1"), new BigDecimal("500"));

        var txs = txService.getTransactionsByUserId(userId);
        assertEquals(1, txs.size());
        assertEquals(TransactionType.BUY, txs.get(0).getType());
        assertEquals("BTC", txs.get(0).getAssetTicker());
    }

    @Test
    @DisplayName("sell() creates a SELL transaction record")
    void sellCreatesTransaction() {
        walletService.deposit(btcWalletId, new BigDecimal("10"));

        tradeService.sell(userId, usdWalletId, btcWalletId,
                new BigDecimal("1"), new BigDecimal("500"));

        var txs = txService.getTransactionsByUserId(userId);
        assertEquals(1, txs.size());
        assertEquals(TransactionType.SELL, txs.get(0).getType());
        assertEquals("BTC", txs.get(0).getAssetTicker());
    }

    @Test
    @DisplayName("buy() throws for negative amount")
    void buyThrowsForNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> tradeService.buy(userId, usdWalletId, btcWalletId,
                        new BigDecimal("-1"), new BigDecimal("100")));
    }

    @Test
    @DisplayName("buy() throws for zero price")
    void buyThrowsForZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> tradeService.buy(userId, usdWalletId, btcWalletId,
                        new BigDecimal("1"), BigDecimal.ZERO));
    }

    @Test
    @DisplayName("sell() throws for null amount")
    void sellThrowsForNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> tradeService.sell(userId, usdWalletId, btcWalletId,
                        null, new BigDecimal("100")));
    }

    @Test
    @DisplayName("Cannot trade an asset for itself")
    void cannotTradeAssetForItself() {
        assertThrows(IllegalArgumentException.class,
                () -> tradeService.buy(userId, usdWalletId, usdWalletId,
                        new BigDecimal("1"), new BigDecimal("1")));
    }

    @Test
    @DisplayName("Full trade cycle: buy then sell, verify final balances")
    void fullTradeCycle() {
        // Starting: USD=10000, BTC=0
        // Buy 1 BTC @ $3000 → USD=7000, BTC=1
        tradeService.buy(userId, usdWalletId, btcWalletId,
                new BigDecimal("1"), new BigDecimal("3000"));

        // Sell 1 BTC @ $4000 → USD=11000, BTC=0
        tradeService.sell(userId, usdWalletId, btcWalletId,
                new BigDecimal("1"), new BigDecimal("4000"));

        BigDecimal usdFinal = walletRepo.findById(usdWalletId).getBalance();
        BigDecimal btcFinal = walletRepo.findById(btcWalletId).getBalance();

        assertEquals(0, new BigDecimal("11000").compareTo(usdFinal),
                "USD: 10000 - 3000 + 4000 = 11000");
        assertEquals(0, BigDecimal.ZERO.compareTo(btcFinal),
                "BTC: 0 + 1 - 1 = 0");

        // Should have 2 transaction records
        assertEquals(2, txService.getTransactionsByUserId(userId).size());
    }
}
