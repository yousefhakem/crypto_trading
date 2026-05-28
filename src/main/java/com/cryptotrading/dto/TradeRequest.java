package com.cryptotrading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/** Request body for POST /api/v1/trades/buy and /api/v1/trades/sell */
public class TradeRequest {

    @NotNull(message = "buyOrderId is required")
    private Long buyOrderId;

    @NotNull(message = "sellOrderId is required")
    private Long sellOrderId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "tradingPairId is required")
    private Integer tradingPairId;

    public TradeRequest() {}

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getBuyOrderId() { return buyOrderId; }
    public void setBuyOrderId(Long buyOrderId) { this.buyOrderId = buyOrderId; }

    public Long getSellOrderId() { return sellOrderId; }
    public void setSellOrderId(Long sellOrderId) { this.sellOrderId = sellOrderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getTradingPairId() { return tradingPairId; }
    public void setTradingPairId(Integer tradingPairId) { this.tradingPairId = tradingPairId; }
}
