package com.cryptotrading.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "trading_pair_id", nullable = false)
    private Integer tradingPairId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderSide side;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderType type;

    @Column(precision = 30, scale = 10)
    private BigDecimal price;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal quantity;

    @Column(name = "filled_quantity", nullable = false, precision = 30, scale = 10)
    private BigDecimal filledQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Order() {
    }

    public Order(UUID userId, Integer tradingPairId, OrderSide side, OrderType type,
            BigDecimal price, BigDecimal quantity) {
        if (userId == null)
            throw new IllegalArgumentException("User is required");
        if (tradingPairId == null)
            throw new IllegalArgumentException("Trading pair is required");
        if (side == null)
            throw new IllegalArgumentException("Side is required");
        if (type == null)
            throw new IllegalArgumentException("Type is required");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (type == OrderType.LIMIT && (price == null || price.compareTo(BigDecimal.ZERO) <= 0))
            throw new IllegalArgumentException("Limit order requires a positive price");

        this.userId = userId;
        this.tradingPairId = tradingPairId;
        this.side = side;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.filledQuantity = BigDecimal.ZERO;
        this.status = OrderStatus.OPEN;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Domain methods ─────────────────────────────────────────────────────

    public void fill(BigDecimal filledAmount) {
        if (this.status != OrderStatus.OPEN)
            throw new IllegalStateException("Only open orders can be filled");
        if (filledAmount == null || filledAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Filled amount must be positive");
        this.filledQuantity = this.filledQuantity.add(filledAmount);
        if (this.filledQuantity.compareTo(this.quantity) >= 0)
            this.status = OrderStatus.FILLED;
    }

    public void cancel() {
        if (this.status != OrderStatus.OPEN)
            throw new IllegalStateException("Only open orders can be cancelled");
        this.status = OrderStatus.CANCELLED;
    }

    // ── Getters ────────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Integer getTradingPairId() {
        return tradingPairId;
    }

    public OrderSide getSide() {
        return side;
    }

    public OrderType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getFilledQuantity() {
        return filledQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}