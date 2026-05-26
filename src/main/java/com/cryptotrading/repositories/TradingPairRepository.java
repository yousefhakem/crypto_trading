package com.cryptotrading.repositories;

import com.cryptotrading.models.TradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradingPairRepository extends JpaRepository<TradingPair, Integer> {

    Optional<TradingPair> findBySymbol(String symbol);

    List<TradingPair> findByActive(Boolean active);
}
