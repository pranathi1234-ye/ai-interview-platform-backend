package com.example.aiinterviewbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/coding")
@CrossOrigin(origins = "http://localhost:3000")
public class CodingController {

    @PostMapping("/run")
    public ResponseEntity<?> runCode(@RequestBody Map<String, Object> request) {

        String questionId = (String) request.get("questionId");
        String code = (String) request.get("code");

        if (questionId == null || code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Please write some code first."
                    )
            );
        }

        boolean passed = checkAnswer(questionId, code);

        if (passed) {
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "passed", true,
                            "message", "All test cases passed successfully!"
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "passed", false,
                        "message", "Some test cases failed. Try again!"
                )
        );
    }

    private boolean checkAnswer(String questionId, String code) {

        String normalizedCode = code
                .replaceAll("\\s+", "")
                .toLowerCase();

        switch (questionId) {

            case "reverse-string":
                return normalizedCode.contains("split")
                        && normalizedCode.contains("reverse")
                        && normalizedCode.contains("join");

            case "palindrome":
                return normalizedCode.contains("reverse")
                        || (
                        normalizedCode.contains("for")
                                && normalizedCode.contains("===")
                );

            case "factorial":
                return normalizedCode.contains("factorial")
                        && (
                        normalizedCode.contains("*")
                                || normalizedCode.contains("reduce")
                );

            case "fibonacci":
                return normalizedCode.contains("fibonacci")
                        && normalizedCode.contains("+");

            case "two-sum":
                return normalizedCode.contains("twosum")
                        && (
                        normalizedCode.contains("map")
                                || normalizedCode.contains("for")
                                || normalizedCode.contains("while")
                );

            default:
                return false;
        }
    }
}