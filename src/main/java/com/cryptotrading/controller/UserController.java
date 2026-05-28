package com.cryptotrading.controller;

import com.cryptotrading.dto.CreateUserRequest;
import com.cryptotrading.models.User;
import com.cryptotrading.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** POST /api/v1/users — Create a new user */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        User created = userService.createUser(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** GET /api/v1/users — List all users */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /** GET /api/v1/users/{id} — Get user by ID */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @NonNull UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /** PUT /api/v1/users/{id} — Update user name */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable @NonNull UUID id,
                                           @Valid @RequestBody CreateUserRequest request) {
        User updated = userService.updateUserName(id, request.getName());
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/v1/users/{id} — Delete user */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
