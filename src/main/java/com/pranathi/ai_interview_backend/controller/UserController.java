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

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {

        User foundUser = userRepository
                .findByEmailAndPassword(
                        user.getEmail(),
                        user.getPassword()
                )
                .orElse(null);

        return foundUser;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User Deleted Successfully";
    }
}