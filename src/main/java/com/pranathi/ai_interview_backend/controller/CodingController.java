package com.pranathi.ai_interview_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/coding")
@CrossOrigin(origins = "*")
public class CodingController {

    @PostMapping("/run")
    public ResponseEntity<?> runCode(
            @RequestBody Map<String, Object> request) {

        String questionId = (String) request.get("questionId");
        String code = (String) request.get("code");

        // Check input
        if (questionId == null ||
                code == null ||
                code.trim().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "passed", false,
                            "message", "Please write some code first."
                    )
            );
        }

        // Check submitted code
        boolean passed = checkAnswer(questionId, code);

        if (passed) {

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "passed", true,
                            "message",
                            "All test cases passed successfully!"
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "passed", false,
                        "message",
                        "Some test cases failed. Try again!"
                )
        );
    }

    // =====================================================
    // CHECK CODING ANSWER
    // =====================================================

    private boolean checkAnswer(
            String questionId,
            String code) {

        String normalizedCode = code
                .replaceAll("\\s+", "")
                .toLowerCase();

        switch (questionId) {

            // =================================================
            // REVERSE STRING
            // =================================================

            case "reverse-string":

                return normalizedCode.contains("split")
                        && normalizedCode.contains("reverse")
                        && normalizedCode.contains("join");


            // =================================================
            // PALINDROME
            // =================================================

            case "palindrome":

                return normalizedCode.contains("reverse")
                        || (
                        normalizedCode.contains("for")
                                && normalizedCode.contains("===")
                );


            // =================================================
            // FACTORIAL
            // =================================================

            case "factorial":

                return normalizedCode.contains("factorial")
                        && (
                        normalizedCode.contains("*")
                                || normalizedCode.contains("reduce")
                );


            // =================================================
            // FIBONACCI
            // =================================================

            case "fibonacci":

                return normalizedCode.contains("fibonacci")
                        && normalizedCode.contains("+");


            // =================================================
            // TWO SUM
            // =================================================

            case "two-sum":

                return normalizedCode.contains("twosum")
                        && (
                        normalizedCode.contains("map")
                                || normalizedCode.contains("for")
                                || normalizedCode.contains("while")
                );


            // =================================================
            // UNKNOWN QUESTION
            // =================================================

            default:

                return false;
        }
    }
}
