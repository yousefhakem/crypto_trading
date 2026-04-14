package com.cryptotrading.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Asset {

    private final String ticker;
    private final String symbol;
    private final String name;
    private final AssetType type;
    private LocalDateTime last_updated;

    public Asset(String ticker, String symbol, String name, AssetType type, LocalDateTime last_updated) {
        this.ticker = ticker;
        this.symbol = symbol;
        this.name = name;
        this.type = type;
        this.last_updated = last_updated;
    }

    public String getTicker() {
        return ticker;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public AssetType getType() {
        return type;
    }

    public LocalDateTime getLast_updated() {
        return last_updated;
    }
}
