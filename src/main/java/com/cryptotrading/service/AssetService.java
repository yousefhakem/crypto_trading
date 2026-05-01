package com.cryptotrading.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cryptotrading.exception.DuplicateAssetException;
import com.cryptotrading.exception.ResourceNotFoundException;
import com.cryptotrading.models.Asset;
import com.cryptotrading.models.AssetType;
import com.cryptotrading.repositories.AssetRepository;

@Service
@Transactional
public class AssetService {
    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Transactional(readOnly = true)
    public Asset checkIfAssetExists(String symbol) {
        return assetRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Asset", symbol));
    }

    @Transactional
    public Asset createAsset(String name, String symbol, AssetType type, int decimals, boolean active) {
        if (assetRepository.existsBySymbol(symbol))
            throw new DuplicateAssetException();
        return assetRepository.save(new Asset(name, symbol, type, decimals, active));
    }
}
