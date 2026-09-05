package com.pranathi.ai_interview_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pranathi.ai_interview_backend.dto.FeedbackResult;
import com.pranathi.ai_interview_backend.dto.InterviewAnswer;
import com.pranathi.ai_interview_backend.service.GeminiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public FeedbackController(
            GeminiService geminiService,
            ObjectMapper objectMapper) {

        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<List<FeedbackResult>>
    evaluateAnswers(
            @RequestBody Map<String, List<InterviewAnswer>> request) {

        List<InterviewAnswer> answers =
                request.get("answers");

        List<FeedbackResult> results =
                new ArrayList<>();

        if (answers == null || answers.isEmpty()) {
            return ResponseEntity.ok(results);
        }

        for (InterviewAnswer item : answers) {

            String question =
                    item.getQuestion() == null
                            ? ""
                            : item.getQuestion();

            String answer =
                    item.getAnswer() == null
                            ? ""
                            : item.getAnswer().trim();

            if (answer.isEmpty()) {

                results.add(
                        new FeedbackResult(
                                question,
                                "",
                                0,
                                "No answer was provided.",
                                "Provide an answer before moving to the next question."
                        )
                );

                continue;
            }

            try {

                String aiResponse =
                        geminiService.evaluateAnswer(
                                question,
                                answer
                        );

                JsonNode json =
                        objectMapper.readTree(
                                aiResponse
                        );

                int score =
                        json.path("score").asInt(0);

                score = Math.max(
                        0,
                        Math.min(10, score)
                );

                String feedback =
                        json.path("feedback")
                                .asText(
                                        "No feedback available."
                                );

                String suggestion =
                        json.path("suggestion")
                                .asText(
                                        "Continue practising."
                                );

                results.add(
                        new FeedbackResult(
                                question,
                                answer,
                                score,
                                feedback,
                                suggestion
                        )
                );

            } catch (Exception e) {

                results.add(
                        new FeedbackResult(
                                question,
                                answer,
                                0,
                                "AI evaluation failed.",
                                "Please try generating feedback again."
                        )
                );

                System.err.println(
                        "Evaluation error: "
                                + e.getMessage()
                );
            }
        }

        return ResponseEntity.ok(results);
    }
}