package com.example.demo.contoller;

import com.example.demo.entity.User;
import com.example.demo.service.AuthService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        try {
            User registeredUser = authService.signup(user);

            registeredUser.setPassword(null);

            return ResponseEntity.ok(registeredUser);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            User loggedInUser =
                    authService.login(
                            user.getEmail(),
                            user.getPassword()
                    );

            loggedInUser.setPassword(null);

            return ResponseEntity.ok(loggedInUser);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> request) {

        try {

            String email = request.get("email");
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            authService.changePassword(
                    email,
                    currentPassword,
                    newPassword
            );

            return ResponseEntity.ok(
                    "Password updated successfully"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}