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

    // Register User
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {

    System.out.println("========== LOGIN ==========");

    System.out.println("Email Received: " + user.getEmail());

    System.out.println("Password Received: " + user.getPassword());

    User foundUser = userRepository
            .findByEmailAndPassword(
                    user.getEmail(),
                    user.getPassword()
            )
            .orElse(null);

    if(foundUser == null){
        System.out.println("❌ User NOT Found");
    }else{
        System.out.println("✅ User Found");
        System.out.println(foundUser.getEmail());
    }

    System.out.println("===========================");

    return foundUser;
}
    // Get All Users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User By Id
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Delete User
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "User Deleted Successfully";
    }
}