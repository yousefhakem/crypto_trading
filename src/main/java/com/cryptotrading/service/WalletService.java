package com.cryptotrading.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.cryptotrading.models.Wallet;
import com.cryptotrading.repositories.WalletRepository;

public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(UUID userId, String assetTicker) {
        // Rule 1: Prevent duplicates for the SAME asset
        if (walletRepository.findByUserIdAndAssetTicker(userId, assetTicker) != null) {
            throw new RuntimeException("This user already has a wallet for this asset!");
        }

        Wallet newWallet = new Wallet(UUID.randomUUID(), userId, assetTicker);
        walletRepository.save(newWallet);
        return newWallet;
    }

    public Wallet getWalletsByUserId(UUID userId) {
        return walletRepository.findByUserId(userId);
    }

    public Wallet getWalletById(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found!");
        }
        return wallet;
    }

    public void deposit(UUID walletId, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = walletRepository.findById(walletId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found!");
        }
        wallet.add(amount);
        walletRepository.save(wallet);
    }

    public void withdraw(UUID walletId, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = walletRepository.findById(walletId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found!");
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }
        wallet.sub(amount);
        walletRepository.save(wallet);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
