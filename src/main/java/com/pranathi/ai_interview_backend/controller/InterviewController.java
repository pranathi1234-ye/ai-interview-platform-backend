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
@CrossOrigin(origins = "http://localhost:3000")
public class InterviewController {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public InterviewController(
            GeminiService geminiService,
            ObjectMapper objectMapper) {

        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    // =====================================================
    // GENERATE INTERVIEW QUESTIONS
    // =====================================================

    @PostMapping("/generate-questions")
    public ResponseEntity<?> generateQuestions(
            @RequestBody Map<String, Object> request) {

        try {

            // =================================================
            // LANGUAGE
            // =================================================

            String language = "";

            if (request.get("language") != null) {
                language = request
                        .get("language")
                        .toString()
                        .trim();
            }

            // =================================================
            // DIFFICULTY
            // =================================================

            String difficulty = "Intermediate";

            if (request.get("difficulty") != null) {

                difficulty = request
                        .get("difficulty")
                        .toString()
                        .trim();

            }

            // =================================================
            // INTERVIEW TYPE
            // =================================================

            String interviewType = "Mixed";

            if (request.get("interviewType") != null) {

                interviewType = request
                        .get("interviewType")
                        .toString()
                        .trim();

            }

            // =================================================
            // NUMBER OF QUESTIONS
            // =================================================

            int numberOfQuestions = 10;

            if (request.get("numberOfQuestions") != null) {

                try {

                    numberOfQuestions =
                            Integer.parseInt(
                                    request
                                            .get("numberOfQuestions")
                                            .toString()
                            );

                } catch (NumberFormatException e) {

                    numberOfQuestions = 10;

                }
            }

            // =================================================
            // VALIDATE LANGUAGE
            // =================================================

            if (language.isEmpty()) {

                Map<String, Object> error =
                        new HashMap<>();

                error.put(
                        "message",
                        "Programming language is required."
                );

                return ResponseEntity
                        .badRequest()
                        .body(error);
            }

            // =================================================
            // VALIDATE DIFFICULTY
            // =================================================

            if (difficulty.isEmpty()) {
                difficulty = "Intermediate";
            }

            // =================================================
            // VALIDATE INTERVIEW TYPE
            // =================================================

            if (interviewType.isEmpty()) {
                interviewType = "Mixed";
            }

            // =================================================
            // LIMIT QUESTION COUNT
            // =================================================

            numberOfQuestions =
                    Math.max(
                            1,
                            Math.min(
                                    20,
                                    numberOfQuestions
                            )
                    );

            // =================================================
            // PRINT REQUEST
            // =================================================

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "AI INTERVIEW REQUEST"
            );

            System.out.println(
                    "Language: " + language
            );

            System.out.println(
                    "Difficulty: " + difficulty
            );

            System.out.println(
                    "Interview Type: " + interviewType
            );

            System.out.println(
                    "Number of Questions: "
                            + numberOfQuestions
            );

            System.out.println(
                    "========================================"
            );

            // =================================================
            // CALL GEMINI
            // =================================================

            String aiResponse =
                    geminiService
                            .generateLanguageInterviewQuestions(
                                    language,
                                    difficulty,
                                    interviewType,
                                    numberOfQuestions
                            );

            // =================================================
            // PARSE GEMINI RESPONSE
            // =================================================

            JsonNode json =
                    objectMapper.readTree(
                            aiResponse
                    );

            // =================================================
            // RETURN RESPONSE
            // =================================================

            return ResponseEntity.ok(json);

        } catch (Exception e) {

            e.printStackTrace();

            Map<String, Object> error =
                    new HashMap<>();

            error.put(
                    "message",
                    e.getMessage() != null
                            ? e.getMessage()
                            : "Unable to generate interview questions."
            );

            return ResponseEntity
                    .internalServerError()
                    .body(error);
        }
    }
}