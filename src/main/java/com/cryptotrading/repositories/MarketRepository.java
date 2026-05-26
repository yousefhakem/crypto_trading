package com.cryptotrading.repositories;

import com.cryptotrading.models.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MarketRepository extends JpaRepository<Market, UUID> {
    // assetId is the primary key — findById() covers the main lookup.
    // Additional query methods can be added here as needed.
}
