package com.cryptotrading.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "markets")
public class Market {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID tradingPairId;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    protected Market() {
    }

    public Market(UUID tradingPairId, BigDecimal price) {
        if (tradingPairId == null) {
            throw new IllegalArgumentException("Trading pair is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.tradingPairId = tradingPairId;
        this.price = price;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public UUID getTradingPairId() {
        return tradingPairId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
