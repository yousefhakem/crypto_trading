package com.cryptotrading.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.cryptotrading.models.Transaction;
import com.cryptotrading.models.TransactionType;
import com.cryptotrading.repositories.TransactionRepository;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(UUID userId, UUID walletId, TransactionType type, String assetTicker,
            BigDecimal amount, BigDecimal price) {
        Transaction transaction = new Transaction(UUID.randomUUID(), userId, walletId, type, assetTicker, amount,
                price);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByUserId(UUID userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByWalletId(UUID walletId) {
        return transactionRepository.findByWalletId(walletId);
    }
}
