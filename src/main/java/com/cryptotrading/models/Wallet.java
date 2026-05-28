package com.cryptotrading.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.cryptotrading.exception.InsufficientBalanceException;

@Entity
@Table(name = "wallets", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "asset_ticker" }))
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "asset_symbol", nullable = false, length = 20)
    private String assetSymbol;

    @Column(nullable = false, precision = 30, scale = 10)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Wallet() {
    }

    public Wallet(UUID userId, String assetSymbol) {
        this.userId = userId;
        this.assetSymbol = assetSymbol;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ── Balance mutations ──────────────────────────────────────────────────────

    /** Adds {@code amount} to the current balance. */
    public void add(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        this.balance = this.balance.add(amount);
    }

    /** Subtracts {@code amount} from the current balance. */
    public void sub(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance!");
        }
        this.balance = this.balance.subtract(amount);
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
