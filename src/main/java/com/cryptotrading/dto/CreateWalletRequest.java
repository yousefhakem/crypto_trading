package com.cryptotrading.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Request body for POST /api/v1/users/{userId}/wallets */
public class CreateWalletRequest {

    @NotBlank(message = "Asset ticker is required")
    @Size(max = 20, message = "Asset ticker must be 20 characters or fewer")
    private String assetTicker;

    public CreateWalletRequest() {}

    public CreateWalletRequest(String assetTicker) {
        this.assetTicker = assetTicker;
    }

    public String getAssetTicker() {
        return assetTicker;
    }

    public void setAssetTicker(String assetTicker) {
        this.assetTicker = assetTicker;
    }
}
