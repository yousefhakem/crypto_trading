package com.cryptotrading.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private String name;
    private final UUID id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String name, UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
