package com.pranathi.ai_interview_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-3.5-flash-lite}")
    private String model;

    public GeminiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    // =========================================================
    // 1. EVALUATE INTERVIEW ANSWER
    // =========================================================

    public String evaluateAnswer(
            String question,
            String answer) {

        String prompt = """
                You are an expert technical interview evaluator.

                Evaluate the candidate's answer carefully.

                Interview Question:
                %s

                Candidate Answer:
                %s

                Evaluate based on:

                1. Correctness
                2. Relevance
                3. Technical understanding
                4. Clarity
                5. Completeness

                Return ONLY valid JSON.

                Required format:

                {
                  "score": 8,
                  "feedback": "Brief evaluation of the answer.",
                  "suggestion": "Specific advice to improve the answer."
                }

                Rules:

                - score must be an integer from 0 to 10
                - feedback must be constructive
                - suggestion must be practical
                - do not use markdown
                - do not use code fences
                - return only JSON
                """.formatted(
                question,
                answer
        );

        return callGemini(prompt, true);
    }

    // =========================================================
    // 2. ANALYZE RESUME
    // =========================================================

    public String analyzeResume(String resumeText) {

        String prompt = """
                You are an expert ATS Resume Analyzer.

                Analyze the following resume carefully.

                =========================
                RESUME
                =========================

                %s

                =========================
                ANALYSIS REQUIREMENTS
                =========================

                Evaluate:

                - Overall resume quality
                - Technical skills
                - Projects
                - Experience
                - Achievements
                - Strengths
                - Weaknesses
                - ATS friendliness
                - Areas for improvement

                Return ONLY valid JSON.

                Required format:

                {
                  "score": 90,
                  "summary": "Professional summary of the resume.",
                  "skills": [
                    "Skill 1",
                    "Skill 2",
                    "Skill 3"
                  ],
                  "strengths": [
                    "Strength 1",
                    "Strength 2",
                    "Strength 3"
                  ],
                  "weaknesses": [
                    "Weakness 1",
                    "Weakness 2",
                    "Weakness 3"
                  ],
                  "recommendations": [
                    "Recommendation 1",
                    "Recommendation 2",
                    "Recommendation 3"
                  ]
                }

                Rules:

                - score must be between 0 and 100
                - skills must come from the actual resume
                - strengths must be based on the actual resume
                - weaknesses must be constructive
                - recommendations must be specific
                - do not invent experience
                - do not invent skills
                - do not use markdown
                - do not use code fences
                - return only JSON
                """.formatted(resumeText);

        return callGemini(prompt, true);
    }

    // =========================================================
    // 3. GENERATE RESUME-BASED INTERVIEW QUESTIONS
    // =========================================================

    public String generateInterviewQuestions(String resumeText) {

        String prompt = """
                You are an expert technical interviewer.

                Analyze the candidate's resume below.

                =========================
                CANDIDATE RESUME
                =========================

                %s

                =========================
                TASK
                =========================

                Generate exactly 10 personalized interview questions.

                Every question must be based on information actually
                present in the resume.

                Cover different areas such as:

                1. Projects
                2. Programming languages
                3. Technical skills
                4. Frontend technologies
                5. Backend technologies
                6. Databases
                7. AI or specialized skills
                8. Experience
                9. Technical decisions
                10. Deep problem-solving

                Questions should gradually become more difficult.

                Avoid generic questions like:

                "What is Java?"

                "What is Python?"

                "What is React?"

                Instead ask questions related to the candidate's
                actual projects, technologies and experience.

                Do NOT invent:

                - Projects
                - Skills
                - Companies
                - Experience
                - Technologies
                - Achievements

                =========================
                OUTPUT
                =========================

                Return ONLY valid JSON.

                Required format:

                {
                  "questions": [
                    "Question 1",
                    "Question 2",
                    "Question 3",
                    "Question 4",
                    "Question 5",
                    "Question 6",
                    "Question 7",
                    "Question 8",
                    "Question 9",
                    "Question 10"
                  ]
                }

                Rules:

                - exactly 10 questions
                - every question must be a string
                - questions must be personalized
                - questions must not repeat
                - no markdown
                - no code fences
                - return only JSON
                """.formatted(resumeText);

        return callGemini(prompt, true);
    }

    // =========================================================
    // 4. PROGRAMMING LANGUAGE INTERVIEW
    // =========================================================

    public String generateLanguageInterviewQuestions(
            String language,
            String difficulty,
            String interviewType,
            int numberOfQuestions) {

        String prompt = """
                You are an expert technical interviewer.

                Conduct a programming interview.

                Programming Language:
                %s

                Difficulty:
                %s

                Interview Type:
                %s

                Number of Questions:
                %d

                Generate exactly %d questions.

                =========================
                DIFFICULTY
                =========================

                Beginner:
                - Basic syntax
                - Fundamental concepts
                - Simple questions

                Intermediate:
                - Practical concepts
                - Problem solving
                - Data structures
                - Moderate complexity

                Advanced:
                - Deep concepts
                - Optimization
                - Performance
                - Architecture
                - Difficult problem solving

                =========================
                INTERVIEW TYPE
                =========================

                Technical:
                - Technical concepts
                - Language features
                - Best practices
                - Performance

                Coding:
                - Coding problems
                - Algorithms
                - Data structures
                - Complexity

                Conceptual:
                - Definitions
                - Differences
                - Advantages
                - Disadvantages
                - Real-world concepts

                Mixed:
                - Technical concepts
                - Coding
                - Problem solving
                - Practical development

                =========================
                LANGUAGE RULE
                =========================

                Questions must be specifically related to:

                %s

                Do not mix unrelated programming languages.

                =========================
                OUTPUT
                =========================

                Return ONLY valid JSON.

                {
                  "language": "%s",
                  "difficulty": "%s",
                  "interviewType": "%s",
                  "questions": [
                    "Question 1",
                    "Question 2"
                  ]
                }

                Rules:

                - exactly %d questions
                - every question must be a string
                - questions must be different
                - questions must match the language
                - questions must match the difficulty
                - questions must match the interview type
                - no markdown
                - no code fences
                - return only JSON
                """.formatted(
                language,
                difficulty,
                interviewType,
                numberOfQuestions,
                numberOfQuestions,
                language,
                language,
                difficulty,
                interviewType,
                numberOfQuestions
        );

        return callGemini(prompt, true);
    }

    // =========================================================
    // 4B. COMPATIBILITY METHOD
    // =========================================================

    public String generateLanguageInterviewQuestions(
            String language,
            int numberOfQuestions) {

        return generateLanguageInterviewQuestions(
                language,
                "Intermediate",
                "Technical",
                numberOfQuestions
        );
    }

    // =========================================================
    // 5. HR CHAT
    // =========================================================

    public String hrChat(String question) {

        String prompt = """
                You are a professional HR interview coach.

                Candidate's question:

                %s

                Help the candidate prepare for an interview.

                Explain when useful:

                1. What the interviewer is looking for
                2. How to structure the answer
                3. A professional sample answer for a fresher

                Keep the response:

                - Professional
                - Clear
                - Practical
                - Interview focused
                - Easy to understand

                Return plain text only.
                """.formatted(question);

        return callGemini(prompt, false);
    }

    // =========================================================
    // 6. GEMINI API
    // =========================================================

    private String callGemini(
            String prompt,
            boolean jsonResponse) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException(
                    "Gemini API key is missing. Set GEMINI_API_KEY before starting Spring Boot."
            );
        }

        List<String> models = new ArrayList<>();

        if (model != null && !model.isBlank()) {
            models.add(model);
        }

        // Fallback models
        addIfMissing(models, "gemini-3.5-flash-lite");
        addIfMissing(models, "gemini-3.6-flash");
        addIfMissing(models, "gemini-3.7-flash");
        addIfMissing(models, "gemini-3.8-flash");

        Exception lastException = null;

        for (String currentModel : models) {

            try {

                System.out.println(
                        "Calling Gemini model: " + currentModel
                );

                return callModel(
                        currentModel,
                        prompt,
                        jsonResponse
                );

            } catch (HttpStatusCodeException e) {

                lastException = e;

                int status =
                        e.getStatusCode().value();

                System.err.println(
                        "Gemini model failed: "
                                + currentModel
                );

                System.err.println(
                        "HTTP status: " + status
                );

                System.err.println(
                        e.getResponseBodyAsString()
                );

                /*
                 * Retry another model for temporary
                 * availability/rate-limit problems.
                 */

                if (status == 429 ||
                        status == 500 ||
                        status == 502 ||
                        status == 503 ||
                        status == 504) {

                    continue;
                }

                throw new RuntimeException(
                        "Gemini API Error: "
                                + e.getResponseBodyAsString(),
                        e
                );

            } catch (Exception e) {

                lastException = e;

                System.err.println(
                        "Gemini error with model "
                                + currentModel
                );

                System.err.println(
                        e.getMessage()
                );
            }
        }

        if (lastException instanceof HttpStatusCodeException e) {

            throw new RuntimeException(
                    "Gemini API is temporarily unavailable. "
                            + "All configured Gemini models failed. "
                            + "Please try again shortly. "
                            + "Last error: "
                            + e.getResponseBodyAsString(),
                    e
            );
        }

        throw new RuntimeException(
                "Unable to communicate with Gemini.",
                lastException
        );
    }

    // =========================================================
    // 7. CALL ONE MODEL
    // =========================================================

    private String callModel(
            String currentModel,
            String prompt,
            boolean jsonResponse) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/"
                        + currentModel
                        + ":generateContent";

        Map<String, Object> textPart =
                Map.of(
                        "text",
                        prompt
                );

        Map<String, Object> content =
                Map.of(
                        "parts",
                        List.of(textPart)
                );

        Map<String, Object> generationConfig;

        if (jsonResponse) {

            generationConfig =
                    Map.of(
                            "responseMimeType",
                            "application/json"
                    );

        } else {

            generationConfig =
                    Map.of();
        }

        Map<String, Object> requestBody;

        if (jsonResponse) {

            requestBody =
                    Map.of(
                            "contents",
                            List.of(content),
                            "generationConfig",
                            generationConfig
                    );

        } else {

            requestBody =
                    Map.of(
                            "contents",
                            List.of(content)
                    );
        }

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        headers.set(
                "x-goog-api-key",
                apiKey
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(
                        requestBody,
                        headers
                );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        String.class
                );

        String responseBody =
                response.getBody();

        if (responseBody == null ||
                responseBody.isBlank()) {

            throw new RuntimeException(
                    "Gemini returned an empty response."
            );
        }

        try {

            JsonNode root =
                    objectMapper.readTree(
                            responseBody
                    );

            JsonNode candidates =
                    root.path("candidates");

            if (!candidates.isArray() ||
                    candidates.isEmpty()) {

                throw new RuntimeException(
                        "Gemini returned no candidates."
                );
            }

            JsonNode parts =
                    candidates
                            .get(0)
                            .path("content")
                            .path("parts");

            if (!parts.isArray() ||
                    parts.isEmpty()) {

                throw new RuntimeException(
                        "Gemini response contains no content."
                );
            }

            String generatedText =
                    parts
                            .get(0)
                            .path("text")
                            .asText();

            if (generatedText == null ||
                    generatedText.isBlank()) {

                throw new RuntimeException(
                        "Gemini returned empty text."
                );
            }

            return cleanGeminiResponse(
                    generatedText
            );

        } catch (Exception e) {

            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(
                    "Unable to parse Gemini response.",
                    e
            );
        }
    }

    // =========================================================
    // 8. CLEAN GEMINI RESPONSE
    // =========================================================

    private String cleanGeminiResponse(
            String response) {

        String cleaned =
                response.trim();

        if (cleaned.startsWith("```json")) {

            cleaned =
                    cleaned.substring(7);
        }

        if (cleaned.startsWith("```")) {

            cleaned =
                    cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {

            cleaned =
                    cleaned.substring(
                            0,
                            cleaned.length() - 3
                    );
        }

        return cleaned.trim();
    }

    // =========================================================
    // 9. ADD FALLBACK MODEL
    // =========================================================

    private void addIfMissing(
            List<String> models,
            String modelName) {

        if (!models.contains(modelName)) {
            models.add(modelName);
        }
    }
}