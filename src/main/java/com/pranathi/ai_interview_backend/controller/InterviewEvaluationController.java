package com.pranathi.ai_interview_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranathi.ai_interview_backend.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "*")
public class InterviewEvaluationController {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public InterviewEvaluationController(
            GeminiService geminiService,
            ObjectMapper objectMapper) {

        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/evaluate-answer")
    public ResponseEntity<?> evaluateAnswer(
            @RequestBody Map<String, Object> request) {

        try {
            String question = request.get("question") == null
                    ? ""
                    : request.get("question").toString().trim();

            String answer = request.get("answer") == null
                    ? ""
                    : request.get("answer").toString().trim();

            if (question.isEmpty() || answer.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Question and answer are required.");
                return ResponseEntity.badRequest().body(error);
            }

            String aiResponse =
                    geminiService.evaluateAnswer(question, answer);

            JsonNode evaluation =
                    objectMapper.readTree(aiResponse);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("evaluation", evaluation);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put(
                    "message",
                    e.getMessage() != null
                            ? e.getMessage()
                            : "Unable to evaluate answer."
            );

            return ResponseEntity
                    .internalServerError()
                    .body(error);
        }
    }
}