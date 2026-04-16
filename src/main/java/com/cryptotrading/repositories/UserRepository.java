package com.cryptotrading.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cryptotrading.models.User;

public class UserRepository {
    private Map<UUID, User> database = new HashMap<>();

    public void save(User user) {
        database.put(user.getId(), user);
    }

    public User findById(UUID id) {
        return database.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    public void deleteById(UUID id) {
        database.remove(id);
    }
}
