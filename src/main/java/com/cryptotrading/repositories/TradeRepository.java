package com.cryptotrading.repositories;

import com.cryptotrading.models.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findByTradingPairId(Integer tradingPairId);

    List<Trade> findByBuyOrderId(Long buyOrderId);

    List<Trade> findBySellOrderId(Long sellOrderId);
}
