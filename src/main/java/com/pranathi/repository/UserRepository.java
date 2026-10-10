
package com.pranathi.ai_interview_backend.repository;

import com.pranathi.ai_interview_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email for login
    Optional<User> findByEmail(String email);

    // Retained for compatibility with existing code
    Optional<User> findByEmailAndPassword(
            String email,
            String password
    );
}
