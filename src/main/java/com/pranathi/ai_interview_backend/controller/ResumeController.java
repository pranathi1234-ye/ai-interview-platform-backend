package com.pranathi.ai_interview_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeController {

    private static final String UPLOAD_DIRECTORY = "uploads/resumes";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadResume(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {

            // Check whether file was selected
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Please select a resume.");

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            // Get original filename
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null ||
                    originalFileName.trim().isEmpty()) {

                response.put("success", false);
                response.put("message", "Invalid file name.");

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            // Validate file extension
            String lowerCaseName = originalFileName.toLowerCase();

            if (!lowerCaseName.endsWith(".pdf") &&
                    !lowerCaseName.endsWith(".doc") &&
                    !lowerCaseName.endsWith(".docx")) {

                response.put("success", false);
                response.put(
                        "message",
                        "Only PDF, DOC and DOCX files are allowed."
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            // Maximum 5 MB
            long maxSize = 5 * 1024 * 1024;

            if (file.getSize() > maxSize) {

                response.put("success", false);
                response.put(
                        "message",
                        "Resume must be smaller than 5 MB."
                );

                return ResponseEntity
                        .badRequest()
                        .body(response);
            }

            // Create upload folder
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );

            String storedFileName =
                    UUID.randomUUID() + extension;

            Path filePath =
                    uploadPath.resolve(storedFileName);

            // Save file
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            response.put("success", true);
            response.put(
                    "message",
                    "Resume uploaded successfully."
            );
            response.put(
                    "originalFileName",
                    originalFileName
            );
            response.put(
                    "storedFileName",
                    storedFileName
            );
            response.put(
                    "size",
                    file.getSize()
            );

            return ResponseEntity.ok(response);

        } catch (IOException e) {

            e.printStackTrace();

            response.put("success", false);
            response.put(
                    "message",
                    "Could not save the resume."
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}