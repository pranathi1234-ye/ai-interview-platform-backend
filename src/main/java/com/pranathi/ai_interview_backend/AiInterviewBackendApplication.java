package com.pranathi.ai_interview_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.pranathi.entity")
@EnableJpaRepositories(basePackages = "com.pranathi.repository")
public class AiInterviewBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiInterviewBackendApplication.class, args);
    }
}