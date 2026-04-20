package com.cryptotrading.repositories;

import com.cryptotrading.models.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WalletRepository Tests")
class WalletRepositoryTest {

    private WalletRepository repo;

    @BeforeEach
    void setUp() {
        repo = new WalletRepository();
    }

    private Wallet createWallet(UUID userId, String ticker) {
        return new Wallet(UUID.randomUUID(), userId, ticker);
    }

    @Test
    @DisplayName("save and findById returns the same wallet")
    void saveAndFindById() {
        Wallet wallet = createWallet(UUID.randomUUID(), "BTC");
        repo.save(wallet);

        Wallet found = repo.findById(wallet.getId());
        assertNotNull(found);
        assertEquals(wallet.getId(), found.getId());
    }

    @Test
    @DisplayName("findById returns null for unknown ID")
    void findByIdReturnsNullForUnknown() {
        assertNull(repo.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("findByUserId returns wallet belonging to user")
    void findByUserIdReturnsCorrectWallet() {
        UUID userId = UUID.randomUUID();
        Wallet wallet = createWallet(userId, "USD");
        repo.save(wallet);

        Wallet found = repo.findByUserId(userId);
        assertNotNull(found);
        assertEquals(userId, found.getUserId());
        assertEquals(wallet.getId(), found.getId());
    }

    @Test
    @DisplayName("findByUserId returns null when user has no wallet")
    void findByUserIdReturnsNullForUnknown() {
        repo.save(createWallet(UUID.randomUUID(), "BTC"));

        assertNull(repo.findByUserId(UUID.randomUUID()));
    }

    @Test
    @DisplayName("findByUserIdAndAssetTicker returns matching wallet")
    void findByUserIdAndAssetTickerReturnsMatch() {
        UUID userId = UUID.randomUUID();
        repo.save(createWallet(userId, "BTC"));
        repo.save(createWallet(userId, "ETH"));

        Wallet found = repo.findByUserIdAndAssetTicker(userId, "ETH");
        assertNotNull(found);
        assertEquals("ETH", found.getAssetTicker());
        assertEquals(userId, found.getUserId());
    }

    @Test
    @DisplayName("findByUserIdAndAssetTicker returns null for non-matching ticker")
    void findByUserIdAndAssetTickerReturnsNullForWrongTicker() {
        UUID userId = UUID.randomUUID();
        repo.save(createWallet(userId, "BTC"));

        assertNull(repo.findByUserIdAndAssetTicker(userId, "ETH"));
    }

    @Test
    @DisplayName("findByUserIdAndAssetTicker returns null for wrong user")
    void findByUserIdAndAssetTickerReturnsNullForWrongUser() {
        repo.save(createWallet(UUID.randomUUID(), "BTC"));

        assertNull(repo.findByUserIdAndAssetTicker(UUID.randomUUID(), "BTC"));
    }
}
