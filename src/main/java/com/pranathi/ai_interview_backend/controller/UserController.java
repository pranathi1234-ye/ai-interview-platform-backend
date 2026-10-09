
package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.dto.UserResponse;
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
    public UserResponse register(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody User user) {
        User foundUser = userRepository
                .findByEmailAndPassword(
                        user.getEmail(),
                        user.getPassword()
                )
                .orElse(null);

        if (foundUser == null) {
            return null;
        }

        return toUserResponse(foundUser);
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(this::toUserResponse)
                .orElse(null);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User Deleted Successfully";
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
