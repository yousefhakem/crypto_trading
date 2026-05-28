package com.cryptotrading.service;

import com.cryptotrading.exception.DuplicateWalletException;
import com.cryptotrading.exception.ResourceNotFoundException;
import com.cryptotrading.models.Wallet;
import com.cryptotrading.repositories.WalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

@Service
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;
    private final AssetService assetService;

    public WalletService(WalletRepository walletRepository, AssetService assetService) {
        this.walletRepository = walletRepository;
        this.assetService = assetService;
    }

    @Transactional
    public Wallet createWallet(UUID userId, String assetSymbol) {
        assetService.checkIfAssetExists(assetSymbol);

        if (walletRepository.existsByUserIdAndAssetSymbol(userId, assetSymbol))
            throw new DuplicateWalletException("This user already has a wallet for this asset!");

        return walletRepository.save(new Wallet(userId, assetSymbol));
    }

    @Transactional(readOnly = true)
    public List<Wallet> getWalletsByUserId(UUID userId) {
        return walletRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Wallet getWalletById(@NonNull UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", walletId));
    }

    public void deposit(@NonNull UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", walletId));
        wallet.add(amount);
    }

    public void withdraw(@NonNull UUID walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet", walletId));
        wallet.sub(amount); // model validates amount > 0 and balance sufficiency
    }
}
