package com.cryptotrading.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

    @Scheduled(fixedRateString = "${schedule.market-price-updates:1000}")
    public void marketPriceUpdates() {
        System.out.println("Market price updates");
    }
}
