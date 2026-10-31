package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // SIGNUP
    public User signup(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // LOGIN
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
 // CHANGE PASSWORD
    public void changePassword(
            String email,
            String currentPassword,
            String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(
                currentPassword,
                user.getPassword())) {

            throw new RuntimeException("Current password is incorrect");
        }

        // Prevent same password
        if (passwordEncoder.matches(
                newPassword,
                user.getPassword())) {

            throw new RuntimeException(
                    "New password must be different from the current password");
        }

        // Encrypt new password
        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        // Save to MySQL
        userRepository.save(user);
    }
}