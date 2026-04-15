package com.cryptotrading.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Market {
    private final UUID assetId;
    private BigDecimal price;
    private LocalDateTime lastUpdated;

    public Market(UUID assetId, BigDecimal price, LocalDateTime lastUpdated) {
        this.assetId = assetId;
        this.price = price;
        this.lastUpdated = lastUpdated;
    }

    public UUID getAssetId() {
        return assetId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
