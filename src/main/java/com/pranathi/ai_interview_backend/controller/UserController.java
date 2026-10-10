package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.dto.UserResponse;
import com.pranathi.ai_interview_backend.entity.User;
import com.pranathi.ai_interview_backend.repository.UserRepository;
import com.pranathi.ai_interview_backend.service.PasswordService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://ai-interview-platform-frontend-5rbw.onrender.com"
})
public class UserController {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserController(
            UserRepository userRepository,
            PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (user.getName() == null
                || user.getEmail() == null
                || user.getPassword() == null
                || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Name, email, and password are required.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already registered.");
        }

        user.setPassword(
                passwordService.hashPassword(user.getPassword())
        );

        User savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toUserResponse(savedUser));
    }

    // Login an existing user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        if (user.getEmail() == null
                || user.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body("Email and password are required.");
        }

        Optional<User> optionalUser =
                userRepository.findByEmail(user.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }

        User foundUser = optionalUser.get();
        String storedPassword = foundUser.getPassword();
        String submittedPassword = user.getPassword();

        if (storedPassword == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }

        boolean passwordMatches;

        if (storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {

            passwordMatches = passwordService.verifyPassword(
                    submittedPassword,
                    storedPassword
            );

        } else {
            // Temporary migration for existing plain-text passwords
            passwordMatches =
                    storedPassword.equals(submittedPassword);

            if (passwordMatches) {
                foundUser.setPassword(
                        passwordService.hashPassword(submittedPassword)
                );
                userRepository.save(foundUser);
            }
        }

        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }

        return ResponseEntity.ok(toUserResponse(foundUser));
    }

    // Get all users without exposing passwords
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    // Get one user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found.");
        }

        return ResponseEntity.ok(toUserResponse(user.get()));
    }

    // Delete a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found.");
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok("User deleted successfully.");
    }
}
