
package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.dto.UserResponse;
import com.pranathi.ai_interview_backend.entity.User;
import com.pranathi.ai_interview_backend.repository.UserRepository;
import com.pranathi.ai_interview_backend.service.PasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "https://ai-interview-platform-frontend-5rbw.onrender.com"
})
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody User user) {

        user.setPassword(
                passwordService.hashPassword(user.getPassword())
        );

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody User user) {

        User foundUser = userRepository
                .findByEmail(user.getEmail())
                .orElse(null);

        if (foundUser == null ||
                !passwordService.verifyPassword(
                        user.getPassword(),
                        foundUser.getPassword())) {
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
