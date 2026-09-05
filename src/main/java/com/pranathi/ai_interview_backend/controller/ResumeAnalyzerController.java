package com.pranathi.ai_interview_backend.controller;

import com.pranathi.ai_interview_backend.service.GeminiService;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeAnalyzerController {

    private final GeminiService geminiService;

    public ResumeAnalyzerController(
            GeminiService geminiService) {

        this.geminiService =
                geminiService;
    }

    // =========================================================
    // 1. EXTRACT RESUME TEXT
    // =========================================================

    @PostMapping("/resume-analyzer/extract")
    public ResponseEntity<Map<String, Object>> extractResume(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            if (file == null ||
                    file.isEmpty()) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Please select a resume."
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            String fileName =
                    file.getOriginalFilename();

            if (fileName == null ||
                    !fileName
                            .toLowerCase()
                            .endsWith(".pdf")) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Only PDF resumes are allowed."
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            long maxSize =
                    5 * 1024 * 1024;

            if (file.getSize() > maxSize) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Resume must be smaller than 5 MB."
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            byte[] pdfBytes =
                    file.getBytes();

            String extractedText;

            try (
                    PDDocument document =
                            Loader.loadPDF(pdfBytes)
            ) {

                PDFTextStripper stripper =
                        new PDFTextStripper();

                extractedText =
                        stripper.getText(document);
            }

            if (extractedText == null ||
                    extractedText
                            .trim()
                            .isEmpty()) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "No readable text found in the PDF."
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Resume extracted successfully."
            );

            response.put(
                    "fileName",
                    fileName
            );

            response.put(
                    "text",
                    extractedText
            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.put(
                    "success",
                    false
            );

            response.put(
                    "message",
                    "Error reading resume: "
                            + e.getMessage()
            );

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(response);
        }
    }

    // =========================================================
    // 2. AI RESUME ANALYSIS
    // =========================================================

    @PostMapping("/resume-analyzer/analyze")
    public ResponseEntity<?> analyzeResume(
            @RequestBody Map<String, String> request) {

        try {

            String resumeText =
                    request.get("resumeText");

            if (resumeText == null ||
                    resumeText
                            .trim()
                            .isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Resume text is empty."
                                )
                        );
            }

            String analysis =
                    geminiService
                            .analyzeResume(
                                    resumeText
                            );

            return ResponseEntity.ok(
                    Map.of(
                            "success",
                            true,
                            "analysis",
                            analysis
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
                                    e.getMessage()
                            )
                    );
        }
    }

    // =========================================================
    // 3. GENERATE RESUME QUESTIONS
    // =========================================================

    @PostMapping("/resume-analyzer/generate-questions")
    public ResponseEntity<?> generateInterviewQuestions(
            @RequestBody Map<String, String> request) {

        try {

            String resumeText =
                    request.get("resumeText");

            if (resumeText == null ||
                    resumeText
                            .trim()
                            .isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Resume text is empty."
                                )
                        );
            }

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Generating resume-based questions"
            );

            System.out.println(
                    "Resume characters: "
                            + resumeText.length()
            );

            System.out.println(
                    "========================================"
            );

            String questions =
                    geminiService
                            .generateInterviewQuestions(
                                    resumeText
                            );

            return ResponseEntity.ok(
                    Map.of(
                            "success",
                            true,
                            "questions",
                            questions
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
                                    e.getMessage()
                            )
                    );
        }
    }
}