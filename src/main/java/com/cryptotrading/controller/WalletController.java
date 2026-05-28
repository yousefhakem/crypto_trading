package com.cryptotrading.controller;

import com.cryptotrading.dto.AmountRequest;
import com.cryptotrading.dto.CreateWalletRequest;
import com.cryptotrading.models.Wallet;
import com.cryptotrading.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /** POST /api/v1/users/{userId}/wallets — Create wallet for user */
    @PostMapping("/users/{userId}/wallets")
    public ResponseEntity<Wallet> createWallet(@PathVariable UUID userId,
                                               @Valid @RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(userId, request.getAssetTicker());
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    /** GET /api/v1/users/{userId}/wallets — List all wallets for a user */
    @GetMapping("/users/{userId}/wallets")
    public ResponseEntity<List<Wallet>> getWalletsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.getWalletsByUserId(userId));
    }

    /** GET /api/v1/wallets/{id} — Get a single wallet */
    @GetMapping("/wallets/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable @NonNull UUID id) {
        return ResponseEntity.ok(walletService.getWalletById(id));
    }

    /** POST /api/v1/wallets/{id}/deposit — Deposit funds into wallet */
    @PostMapping("/wallets/{id}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable @NonNull UUID id,
                                          @Valid @RequestBody AmountRequest request) {
        walletService.deposit(id, request.getAmount());
        return ResponseEntity.ok(walletService.getWalletById(id));
    }

    /** POST /api/v1/wallets/{id}/withdraw — Withdraw funds from wallet */
    @PostMapping("/wallets/{id}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable @NonNull UUID id,
                                           @Valid @RequestBody AmountRequest request) {
        walletService.withdraw(id, request.getAmount());
        return ResponseEntity.ok(walletService.getWalletById(id));
    }
}
