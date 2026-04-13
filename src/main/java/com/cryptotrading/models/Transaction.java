package com.cryptotrading.models;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private UUID id;
    private UUID userId;
    private UUID walletId;
    private TransactionType type; // Fixed Enum syntax
    private String assetTicker;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal totalCost;
    private LocalDateTime createdAt; // Upgraded Date API

    public Transaction(UUID id, UUID userId, UUID walletId, TransactionType type,
            String assetTicker, BigDecimal amount, BigDecimal price) {
        this.id = id;
        this.userId = userId;
        this.walletId = walletId;
        this.type = type;
        this.assetTicker = assetTicker;
        this.amount = amount;
        this.price = price;
        this.totalCost = amount.multiply(price);
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public TransactionType getType() {
        return type;
    }

    public String getAssetTicker() {
        return assetTicker;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}