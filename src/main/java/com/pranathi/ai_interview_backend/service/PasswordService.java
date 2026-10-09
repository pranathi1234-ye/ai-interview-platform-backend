
package com.pranathi.ai_interview_backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(
            String rawPassword,
            String hashedPassword
    ) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }

        return passwordEncoder.matches(
                rawPassword,
                hashedPassword
        );
    }
}
