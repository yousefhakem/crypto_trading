package com.cryptotrading.service;

import com.cryptotrading.exception.InsufficientBalanceException;
import com.cryptotrading.exception.ResourceNotFoundException;
import com.cryptotrading.models.Order;
import com.cryptotrading.models.OrderStatus;
import com.cryptotrading.models.Trade;
import com.cryptotrading.models.TradingPair;
import com.cryptotrading.models.Wallet;
import com.cryptotrading.repositories.OrderRepository;
import com.cryptotrading.repositories.TradeRepository;
import com.cryptotrading.repositories.TradingPairRepository;
import com.cryptotrading.repositories.WalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TradeService {

    private final WalletRepository walletRepository;
    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;
    private final TradingPairRepository tradingPairRepository;

    public TradeService(WalletRepository walletRepository, TradeRepository tradeRepository,
            OrderRepository orderRepository, TradingPairRepository tradingPairRepository) {
        this.walletRepository = walletRepository;
        this.tradeRepository = tradeRepository;
        this.orderRepository = orderRepository;
        this.tradingPairRepository = tradingPairRepository;
    }

    @Transactional
    public void trade(Long buyOrderId, Long sellOrderId, BigDecimal amount, BigDecimal price, Integer tradingPairId) {
        validateTradeInputs(buyOrderId, sellOrderId, amount, price, tradingPairId);

        Order buyOrder = orderRepository.findById(buyOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", buyOrderId));
        Order sellOrder = orderRepository.findById(sellOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", sellOrderId));
        TradingPair tradingPair = tradingPairRepository.findById(tradingPairId)
                .orElseThrow(() -> new ResourceNotFoundException("TradingPair", tradingPairId));

        UUID buyerUserId = buyOrder.getUserId();
        UUID sellerUserId = sellOrder.getUserId();

        String baseAssetSymbol = tradingPair.getBaseAsset().getSymbol();
        String quoteAssetSymbol = tradingPair.getQuoteAsset().getSymbol();

        // 1. Transfer quote currency: Buyer's quote wallet -> Seller's quote wallet
        BigDecimal quoteAmount = amount.multiply(price);
        Wallet buyerQuoteWallet = walletRepository.findByUserIdAndAssetSymbol(buyerUserId, quoteAssetSymbol)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer Quote Wallet not found for user: " + buyerUserId));
        Wallet sellerQuoteWallet = walletRepository.findByUserIdAndAssetSymbol(sellerUserId, quoteAssetSymbol)
                .orElseThrow(() -> new ResourceNotFoundException("Seller Quote Wallet not found for user: " + sellerUserId));

        buyerQuoteWallet.sub(quoteAmount);
        sellerQuoteWallet.add(quoteAmount);

        // 2. Transfer base currency: Seller's base wallet -> Buyer's base wallet
        Wallet sellerBaseWallet = walletRepository.findByUserIdAndAssetSymbol(sellerUserId, baseAssetSymbol)
                .orElseThrow(() -> new ResourceNotFoundException("Seller Base Wallet not found for user: " + sellerUserId));
        Wallet buyerBaseWallet = walletRepository.findByUserIdAndAssetSymbol(buyerUserId, baseAssetSymbol)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer Base Wallet not found for user: " + buyerUserId));

        sellerBaseWallet.sub(amount);
        buyerBaseWallet.add(amount);

        // 3. Update order statuses (fill quantity and check if completed)
        buyOrder.fill(amount);
        sellOrder.fill(amount);

        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);

        // 4. Record the trade
        tradeRepository.save(new Trade(tradingPairId, buyOrderId, sellOrderId, price, amount));
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private void validateTradeInputs(Long buyOrderId, Long sellOrderId,
            BigDecimal amount, BigDecimal price, Integer tradingPairId) {
        if (buyOrderId == null) {
            throw new IllegalArgumentException("Buy order is required.");
        }
        if (sellOrderId == null) {
            throw new IllegalArgumentException("Sell order is required.");
        }
        if (tradingPairId == null) {
            throw new IllegalArgumentException("Trading pair is required.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
    }
}
