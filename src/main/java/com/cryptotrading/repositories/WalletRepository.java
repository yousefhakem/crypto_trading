package com.cryptotrading.repositories;

import com.cryptotrading.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    /**
     * Finds a wallet by the owner and asset ticker (enforces uniqueness per asset).
     */
    Optional<Wallet> findByUserIdAndAssetSymbol(UUID userId, String assetSymbol);

    /** Returns all wallets belonging to a given user. */
    List<Wallet> findByUserId(UUID userId);

    boolean existsByUserIdAndAssetSymbol(UUID userId, String assetSymbol);
}