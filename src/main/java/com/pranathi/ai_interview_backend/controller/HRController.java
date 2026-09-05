package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.service.GeminiService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "http://localhost:3000")
public class HRController {

    private final GeminiService geminiService;

    public HRController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // =========================================================
    // HR INTERVIEW CHAT
    // =========================================================

    @PostMapping("/chat")
    public ResponseEntity<?> chat(
            @RequestBody Map<String, String> request) {

        try {

            String question =
                    request.get("question");

            // ---------------------------------------------
            // Validate question
            // ---------------------------------------------

            if (question == null ||
                    question.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,

                                        "message",
                                        "Please enter an HR interview question."
                                )
                        );
            }

            // ---------------------------------------------
            // Call Gemini
            // ---------------------------------------------

            String answer =
                    geminiService.hrChat(
                            question.trim()
                    );

            // ---------------------------------------------
            // Return response
            // ---------------------------------------------

            return ResponseEntity.ok(
                    Map.of(
                            "success",
                            true,

                            "answer",
                            answer
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "success",
                                    false,

                                    "message",
                                    e.getMessage() != null
                                            ? e.getMessage()
                                            : "Unable to generate HR response."
                            )
                    );
        }
    }
}