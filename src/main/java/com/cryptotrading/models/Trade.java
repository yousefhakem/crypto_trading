package com.cryptotrading.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trading_pair_id", nullable = false)
    private Integer tradingPairId;

    @Column(name = "buy_order_id", nullable = false)
    private Long buyOrderId;

    @Column(name = "sell_order_id", nullable = false)
    private Long sellOrderId;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal price;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime executedAt;

    protected Trade() {
    }

    public Trade(Integer tradingPairId, Long buyOrderId, Long sellOrderId, BigDecimal price, BigDecimal quantity) {
        if (tradingPairId == null)
            throw new IllegalArgumentException("Trading pair is required");
        if (buyOrderId == null)
            throw new IllegalArgumentException("Buy order is required");
        if (sellOrderId == null)
            throw new IllegalArgumentException("Sell order is required");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        this.tradingPairId = tradingPairId;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    @PrePersist
    protected void onCreate() {
        this.executedAt = LocalDateTime.now();
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public Integer getTradingPairId() {
        return tradingPairId;
    }

    public Long getBuyOrderId() {
        return buyOrderId;
    }

    public Long getSellOrderId() {
        return sellOrderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

}
