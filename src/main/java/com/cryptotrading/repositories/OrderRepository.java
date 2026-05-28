package com.cryptotrading.repositories;

import com.cryptotrading.models.Order;
import com.cryptotrading.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(UUID userId);

    List<Order> findByUserIdAndStatus(UUID userId, OrderStatus status);

    List<Order> findByTradingPairId(Integer tradingPairId);

    List<Order> findByTradingPairIdAndStatus(Integer tradingPairId, OrderStatus status);
}
