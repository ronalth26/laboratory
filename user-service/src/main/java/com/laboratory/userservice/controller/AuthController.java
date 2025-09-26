package com.laboratory.userservice.controller;

import com.laboratory.userservice.model.User;
import com.laboratory.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Información del usuario actual
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        return userService.findByUsername(authentication.getName())
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "roles", user.getRoles(),
                        "enabled", user.isEnabled()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    // Verificar si usuario tiene un rol específico
    @GetMapping("/has-role/{roleName}")
    public ResponseEntity<?> hasRole(Authentication authentication,
                                     @PathVariable String roleName) {
        if (authentication == null) {
            return ResponseEntity.ok(Map.of("hasRole", false));
        }

        boolean hasRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(roleName));

        return ResponseEntity.ok(Map.of("hasRole", hasRole));
    }

    // Endpoint público de health check
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "user-service",
                "timestamp", System.currentTimeMillis()
        ));
    }
}