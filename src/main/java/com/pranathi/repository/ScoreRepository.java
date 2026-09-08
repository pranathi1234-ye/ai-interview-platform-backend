package com.pranathi.ai_interview_backend.repository;

import com.pranathi.ai_interview_backend.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByUsername(String username);
}