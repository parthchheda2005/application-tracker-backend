package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.ApiResponseDto;
import com.applicationtracker.applicationtrackerbackend.model.User;
import com.applicationtracker.applicationtrackerbackend.repository.UserRepository;
import com.applicationtracker.applicationtrackerbackend.service.AzureBlobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/blob")
public class BlobController {

    private final AzureBlobService blobService;
    private final UserRepository userRepository;

    public BlobController(AzureBlobService blobService, UserRepository userRepository) {
        this.blobService = blobService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String blobName = user.getId() + "_"
                    + user.getFirstName() + "_"
                    + user.getLastName() + "_"
                    + file.getOriginalFilename();

            blobService.uploadBlob(blobName, file.getBytes());

            ApiResponseDto response = new ApiResponseDto("File uploaded successfully", true, blobName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto response = new ApiResponseDto("File upload failed: " + e.getMessage(), false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ApiResponseDto> getUrl(@PathVariable String fileName) {
        try {
            String url = blobService.generateBlobSasUrl(fileName, 5);
            ApiResponseDto response = new ApiResponseDto("File uploaded successfully", true, url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto response = new ApiResponseDto("File upload failed: " + e.getMessage(), false);
            return ResponseEntity.status(500).body(response);
        }
    }
}
