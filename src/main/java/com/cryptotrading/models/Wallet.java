package com.cryptotrading.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Wallet {
    private UUID id;
    private UUID userId;
    private final String assetTicker; // <--- Upgraded to Enum
    private BigDecimal balance;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wallet(UUID id, UUID userId, String assetTicker) {
        this.id = id;
        this.userId = userId;
        this.assetTicker = assetTicker;
        this.balance = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void add(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void sub(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public String getAssetTicker() {
        return assetTicker;
    }
}
