package com.cryptotrading.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.cryptotrading.models.Transaction;

public class TransactionRepository {
    // Key is the Transaction ID
    private Map<UUID, Transaction> database = new HashMap<>();

    public void save(Transaction transaction) {
        database.put(transaction.getId(), transaction);
    }

    public Transaction findById(UUID id) {
        return database.get(id);
    }

    public List<Transaction> findByUserId(UUID userId) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : database.values()) {
            if (transaction.getUserId().equals(userId)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public List<Transaction> findByWalletId(UUID walletId) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : database.values()) {
            if (transaction.getWalletId().equals(walletId)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
