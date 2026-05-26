package com.cryptotrading.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "trading_pairs")
public class TradingPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_asset_id", nullable = false)
    private Asset baseAsset;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quote_asset_id", nullable = false)
    private Asset quoteAsset;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @Column(name = "price_precision", nullable = false)
    private Integer pricePrecision;

    @Column(name = "quantity_precision", nullable = false)
    private Integer quantityPrecision;

    @Column(name = "min_order_size", nullable = false, precision = 30, scale = 10)
    private BigDecimal minOrderSize;

    @Column(nullable = false)
    private Boolean active;

    public TradingPair() {
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public Integer getId() {
        return id;
    }

    public Asset getBaseAsset() {
        return baseAsset;
    }

    public Asset getQuoteAsset() {
        return quoteAsset;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public Integer getQuantityPrecision() {
        return quantityPrecision;
    }

    public BigDecimal getMinOrderSize() {
        return minOrderSize;
    }

    public Boolean getActive() {
        return active;
    }
}
