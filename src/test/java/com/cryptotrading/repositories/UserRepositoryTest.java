package com.cryptotrading.repositories;

import com.cryptotrading.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    private UserRepository repo;

    @BeforeEach
    void setUp() {
        repo = new UserRepository();
    }

    private User createUser(String name) {
        return new User(name, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("save and findById returns the same user")
    void saveAndFindById() {
        User user = createUser("yousef");
        repo.save(user);

        User found = repo.findById(user.getId());
        assertNotNull(found);
        assertEquals("yousef", found.getName());
        assertEquals(user.getId(), found.getId());
    }

    @Test
    @DisplayName("findById returns null for unknown ID")
    void findByIdReturnsNullForUnknown() {
        assertNull(repo.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("findAll returns all saved users")
    void findAllReturnsAllUsers() {
        repo.save(createUser("alice"));
        repo.save(createUser("bob"));
        repo.save(createUser("charlie"));

        List<User> all = repo.findAll();
        assertEquals(3, all.size());
    }

    @Test
    @DisplayName("findAll returns a defensive copy")
    void findAllReturnsDefensiveCopy() {
        repo.save(createUser("alice"));
        List<User> list = repo.findAll();

        list.clear(); // mutate the returned list

        // original repo should be unaffected
        assertEquals(1, repo.findAll().size());
    }

    @Test
    @DisplayName("deleteById removes the user")
    void deleteByIdRemovesUser() {
        User user = createUser("toDelete");
        repo.save(user);

        repo.deleteById(user.getId());
        assertNull(repo.findById(user.getId()));
        assertEquals(0, repo.findAll().size());
    }

    @Test
    @DisplayName("save overwrites existing user with same ID")
    void saveOverwritesExisting() {
        User user = createUser("original");
        repo.save(user);

        user.setName("updated");
        repo.save(user);

        assertEquals(1, repo.findAll().size());
        assertEquals("updated", repo.findById(user.getId()).getName());
    }
}
