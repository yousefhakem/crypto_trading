package com.cryptotrading.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.cryptotrading.models.Wallet;

public class WalletRepository {
    // Key is the Wallet ID
    private Map<UUID, Wallet> database = new HashMap<>();

    public void save(Wallet wallet) {
        database.put(wallet.getId(), wallet);
    }

    public Wallet findById(UUID id) {
        return database.get(id);
    }

    public Wallet findByUserIdAndAssetTicker(UUID userId, String assetTicker) {
        for (Wallet wallet : database.values()) {
            if (wallet.getUserId().equals(userId) && wallet.getAssetTicker().equals(assetTicker)) {
                return wallet;
            }
        }
        return null; // Return null if they don't have a wallet for this specific asset
    }

    public Wallet findByUserId(UUID userId) {
        for (Wallet wallet : database.values()) {
            if (wallet.getUserId().equals(userId)) {
                return wallet;
            }
        }
        return null;
    }
}