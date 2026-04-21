package com.cryptotrading.service;

import com.cryptotrading.models.Transaction;
import com.cryptotrading.models.TransactionType;
import com.cryptotrading.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TransactionService Tests")
class TransactionServiceTest {

    private TransactionRepository txRepo;
    private TransactionService txService;
    private UUID userId;
    private UUID walletId;

    @BeforeEach
    void setUp() {
        txRepo = new TransactionRepository();
        txService = new TransactionService(txRepo);
        userId = UUID.randomUUID();
        walletId = UUID.randomUUID();
    }

    @Test
    @DisplayName("createTransaction records and returns a transaction")
    void createTransactionRecordsIt() {
        Transaction tx = txService.createTransaction(userId, walletId,
                TransactionType.BUY, "BTC",
                new BigDecimal("1"), new BigDecimal("5000"));

        assertNotNull(tx);
        assertNotNull(tx.getId());
        assertEquals(TransactionType.BUY, tx.getType());
        assertEquals("BTC", tx.getAssetTicker());
        assertEquals(0, new BigDecimal("5000").compareTo(tx.getTotalCost()));
    }

    @Test
    @DisplayName("createTransaction persists to repository")
    void createTransactionPersists() {
        Transaction tx = txService.createTransaction(userId, walletId,
                TransactionType.SELL, "ETH",
                new BigDecimal("2"), new BigDecimal("3000"));

        Transaction found = txService.getTransactionById(tx.getId());
        assertNotNull(found);
        assertEquals(tx.getId(), found.getId());
    }

    @Test
    @DisplayName("getTransactionById returns null for unknown ID")
    void getByIdReturnsNullForUnknown() {
        assertNull(txService.getTransactionById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("getTransactionsByUserId returns all for a user")
    void getByUserIdReturnsAll() {
        txService.createTransaction(userId, walletId, TransactionType.BUY,
                "BTC", new BigDecimal("1"), new BigDecimal("100"));
        txService.createTransaction(userId, walletId, TransactionType.SELL,
                "ETH", new BigDecimal("1"), new BigDecimal("200"));

        List<Transaction> txs = txService.getTransactionsByUserId(userId);
        assertEquals(2, txs.size());
    }

    @Test
    @DisplayName("getTransactionsByWalletId returns all for a wallet")
    void getByWalletIdReturnsAll() {
        txService.createTransaction(userId, walletId, TransactionType.BUY,
                "BTC", new BigDecimal("1"), new BigDecimal("100"));

        List<Transaction> txs = txService.getTransactionsByWalletId(walletId);
        assertEquals(1, txs.size());
        assertEquals(walletId, txs.get(0).getWalletId());
    }
}
