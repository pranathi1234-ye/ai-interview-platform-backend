package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.entity.User;
import com.pranathi.ai_interview_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ============================================================
    // REGISTER USER
    // ============================================================

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        return userRepository.save(user);
    }

    // ============================================================
    // LOGIN USER
    // ============================================================

    @PostMapping("/login")
    public User login(@RequestBody User user) {

        System.out.println("========== LOGIN ==========");

        // Log only the email.
        // NEVER print the user's password in logs.
        System.out.println("Email Received: " + user.getEmail());

        User foundUser = userRepository
                .findByEmailAndPassword(
                        user.getEmail(),
                        user.getPassword()
                )
                .orElse(null);

        if (foundUser == null) {

            System.out.println("❌ User NOT Found");

        } else {

            System.out.println("✅ User Found");
            System.out.println(
                    "Login successful for: "
                            + foundUser.getEmail()
            );
        }

        System.out.println("===========================");

        return foundUser;
    }

    // ============================================================
    // GET ALL USERS
    // ============================================================

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // ============================================================
    // GET USER BY ID
    // ============================================================

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {

        return userRepository
                .findById(id)
                .orElse(null);
    }

    // ============================================================
    // DELETE USER
    // ============================================================

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "User Deleted Successfully";
    }
}