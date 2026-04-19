package com.cryptotrading.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.cryptotrading.models.Transaction;
import com.cryptotrading.models.TransactionType;
import com.cryptotrading.models.Wallet;

public class TradeService {
    private final TransactionService transactionService;
    private final WalletService walletService;

    public TradeService(TransactionService transactionService, WalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    // Notice we now require BOTH the fiat wallet and the crypto wallet
    // Renamed fiatWalletId -> quoteWalletId (The wallet you pay from)
    // Renamed cryptoWalletId -> baseWalletId (The wallet you receive into)
    public void buy(UUID userId, UUID quoteWalletId, UUID baseWalletId, BigDecimal amount, BigDecimal price) {
        validateAmount(amount);
        validateAmount(price);

        Wallet quoteWallet = walletService.getWalletById(quoteWalletId); // e.g., The BTC Wallet
        Wallet baseWallet = walletService.getWalletById(baseWalletId); // e.g., The ETH Wallet

        // Ensure they aren't trying to trade a coin for the exact same coin
        if (quoteWallet.getAssetTicker().equals(baseWallet.getAssetTicker())) {
            throw new IllegalArgumentException("Cannot trade an asset for itself.");
        }

        // The total cost is calculated in the Quote currency (e.g., paying 0.05 BTC
        // total)
        BigDecimal totalCost = amount.multiply(price);

        // 1. Take the Quote currency (The payment)
        walletService.withdraw(quoteWallet.getId(), totalCost);

        // 2. Give the Base currency (The acquired asset)
        walletService.deposit(baseWallet.getId(), amount);

        // 3. Create the receipt
        transactionService.createTransaction(userId, baseWallet.getId(), TransactionType.BUY,
                baseWallet.getAssetTicker(), amount, price);
    }

    public void sell(UUID userId, UUID quoteWalletId, UUID baseWalletId, BigDecimal amount, BigDecimal price) {
        validateAmount(amount);
        validateAmount(price);

        Wallet quoteWallet = walletService.getWalletById(quoteWalletId); // e.g., The USD Wallet (receiving payment)
        Wallet baseWallet = walletService.getWalletById(baseWalletId); // e.g., The BTC Wallet (giving up asset)

        // Ensure they aren't trying to trade a coin for the exact same coin
        if (quoteWallet.getAssetTicker().equals(baseWallet.getAssetTicker())) {
            throw new IllegalArgumentException("Cannot trade an asset for itself.");
        }

        BigDecimal totalRevenue = amount.multiply(price);

        // 1. Take the Base currency (The sold asset)
        walletService.withdraw(baseWallet.getId(), amount);

        // 2. Give the Quote currency (The payment received)
        walletService.deposit(quoteWallet.getId(), totalRevenue);

        // 3. Log the Trade
        transactionService.createTransaction(userId, baseWallet.getId(), TransactionType.SELL,
                baseWallet.getAssetTicker(), amount, price);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}