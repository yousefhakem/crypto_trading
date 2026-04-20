package com.cryptotrading.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Model Tests")
class UserTest {

    @Test
    @DisplayName("Constructor sets all fields correctly")
    void constructorSetsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = new User("yousef", id, now, now);

        assertEquals("yousef", user.getName());
        assertEquals(id, user.getId());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    @DisplayName("setName updates name and auto-updates updatedAt timestamp")
    void setNameUpdatesTimestamp() {
        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 0, 0);
        User user = new User("old", UUID.randomUUID(), created, created);

        user.setName("new");

        assertEquals("new", user.getName());
        assertTrue(user.getUpdatedAt().isAfter(created),
                "updatedAt should advance after setName()");
    }

    @Test
    @DisplayName("id is immutable after construction")
    void idIsImmutable() {
        // id field is final — no setId() method exists
        // This test verifies the field is accessible only through getId()
        UUID id = UUID.randomUUID();
        User user = new User("test", id, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(id, user.getId());
    }

    @Test
    @DisplayName("createdAt is immutable after construction")
    void createdAtIsImmutable() {
        LocalDateTime created = LocalDateTime.of(2025, 6, 15, 12, 0);
        User user = new User("test", UUID.randomUUID(), created, created);

        // Even after changing the name (which updates updatedAt), createdAt stays the same
        user.setName("changed");
        assertEquals(created, user.getCreatedAt());
    }
}
