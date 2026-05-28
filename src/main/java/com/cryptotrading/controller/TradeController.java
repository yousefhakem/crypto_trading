package com.cryptotrading.controller;

import com.cryptotrading.dto.TradeRequest;
import com.cryptotrading.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /** POST /api/v1/trades — Execute a matched trade */
    @PostMapping
    public ResponseEntity<Map<String, String>> trade(@Valid @RequestBody TradeRequest request) {
        tradeService.trade(
                request.getBuyOrderId(),
                request.getSellOrderId(),
                request.getAmount(),
                request.getPrice(),
                request.getTradingPairId()
        );
        return ResponseEntity.ok(Map.of("message", "Trade executed successfully"));
    }
}
