package com.cryptotrading.repositories;

import com.cryptotrading.models.Transaction;
import com.cryptotrading.models.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TransactionRepository Tests")
class TransactionRepositoryTest {

    private TransactionRepository repo;
    private UUID userId;
    private UUID walletId;

    @BeforeEach
    void setUp() {
        repo = new TransactionRepository();
        userId = UUID.randomUUID();
        walletId = UUID.randomUUID();
    }

    private Transaction createTx(UUID userId, UUID walletId, TransactionType type) {
        return new Transaction(UUID.randomUUID(), userId, walletId, type,
                "BTC", new BigDecimal("1"), new BigDecimal("100"));
    }

    @Test
    @DisplayName("save and findById returns the same transaction")
    void saveAndFindById() {
        Transaction tx = createTx(userId, walletId, TransactionType.BUY);
        repo.save(tx);

        Transaction found = repo.findById(tx.getId());
        assertNotNull(found);
        assertEquals(tx.getId(), found.getId());
    }

    @Test
    @DisplayName("findById returns null for unknown ID")
    void findByIdReturnsNullForUnknown() {
        assertNull(repo.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("findByUserId returns all transactions for a user")
    void findByUserIdReturnsAll() {
        repo.save(createTx(userId, walletId, TransactionType.BUY));
        repo.save(createTx(userId, walletId, TransactionType.SELL));
        repo.save(createTx(UUID.randomUUID(), walletId, TransactionType.BUY)); // different user

        List<Transaction> result = repo.findByUserId(userId);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findByUserId returns empty list when no transactions exist")
    void findByUserIdReturnsEmptyList() {
        List<Transaction> result = repo.findByUserId(UUID.randomUUID());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByWalletId returns all transactions for a wallet")
    void findByWalletIdReturnsAll() {
        UUID otherWallet = UUID.randomUUID();
        repo.save(createTx(userId, walletId, TransactionType.BUY));
        repo.save(createTx(userId, walletId, TransactionType.SELL));
        repo.save(createTx(userId, otherWallet, TransactionType.BUY)); // different wallet

        List<Transaction> result = repo.findByWalletId(walletId);
        assertEquals(2, result.size());
    }
}
