package com.cryptotrading.repositories;

import com.cryptotrading.models.Asset;
import com.cryptotrading.models.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findBySymbol(String symbol);

    List<Asset> findByType(AssetType type);

    List<Asset> findByActive(Boolean active);

    boolean existsBySymbol(String symbol);
}
