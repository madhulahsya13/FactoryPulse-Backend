package com.factorypulse.backend.controller;

import com.factorypulse.backend.dto.RegisterRequest;
import com.factorypulse.backend.model.User;
import com.factorypulse.backend.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Public. Any role sent by the client is ignored: always FACTORY_OWNER. */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        return respond(() -> userService.registerFactoryOwner(request));
    }

    /**
     * Admin creation for the developer (PowerShell). Disabled unless the
     * ADMIN_SETUP_KEY environment variable is set on the server, and the
     * X-Admin-Key header must match it.
     */
    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(
            @RequestHeader(value = "X-Admin-Key", required = false) String key,
            @RequestBody RegisterRequest request) {

        String expected = System.getenv("ADMIN_SETUP_KEY");
        if (expected == null || expected.length() < 16 || key == null
                || !MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                key.getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(403).body(Map.of("message", "Forbidden"));
        }
        return respond(() -> userService.createAdmin(
                request.getName(), request.getEmail(), request.getPassword()));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok("You are authenticated! JWT is working.");
    }

    private ResponseEntity<?> respond(java.util.function.Supplier<User> action) {
        try {
            User u = action.get();
            return ResponseEntity.status(201).body(Map.of(
                    "id", u.getId(), "name", u.getName(),
                    "email", u.getEmail(), "role", u.getRole()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException | DataIntegrityViolationException e) {
            return ResponseEntity.status(409)
                    .body(Map.of("message", "This email is already registered."));
        }
    }
}
