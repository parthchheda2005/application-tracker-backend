package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.ApiResponseDto;
import com.applicationtracker.applicationtrackerbackend.dto.CreateResumeDto;
import com.applicationtracker.applicationtrackerbackend.model.Resume;
import com.applicationtracker.applicationtrackerbackend.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createResume(@RequestBody CreateResumeDto dto) {
        try {
            Resume resume = resumeService.createResume(dto);
            return ResponseEntity.ok(new ApiResponseDto("Successfully created Resume!", true, resume));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("Internal Server Error", false));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllResumes() {
        try {
            List<Resume> resumes = resumeService.getAllResumes();
            return ResponseEntity.ok(new ApiResponseDto("Successfully created Resume!", true, resumes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("Internal Server Error", false));
        }
    }
}
