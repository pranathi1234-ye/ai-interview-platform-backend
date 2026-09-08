package com.pranathi.ai_interview_backend.repository;

import com.pranathi.ai_interview_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(
            String email,
            String password
    );
}