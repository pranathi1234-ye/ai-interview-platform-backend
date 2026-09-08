package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.entity.Score;
import com.pranathi.ai_interview_backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @PostMapping
    public Score saveScore(@RequestBody Score score) {
        return scoreRepository.save(score);
    }

    @GetMapping
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    @GetMapping("/{username}")
    public List<Score> getUserScores(
            @PathVariable String username
    ) {
        return scoreRepository.findByUsername(username);
    }
}