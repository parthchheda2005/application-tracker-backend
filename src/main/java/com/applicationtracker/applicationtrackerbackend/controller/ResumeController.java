package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.ApiResponseDto;
import com.applicationtracker.applicationtrackerbackend.dto.CreateResumeDto;
import com.applicationtracker.applicationtrackerbackend.dto.UpdateResumeDto;
import com.applicationtracker.applicationtrackerbackend.model.Resume;
import com.applicationtracker.applicationtrackerbackend.repository.ResumeRepository;
import com.applicationtracker.applicationtrackerbackend.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;

    public ResumeController(ResumeService resumeService, ResumeRepository resumeRepository) {
        this.resumeService = resumeService;
        this.resumeRepository = resumeRepository;
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
            return ResponseEntity.ok(new ApiResponseDto("Successfully fetched all Resumes!",
                    true,
                    resumes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("Internal Server Error", false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getResumeById(@PathVariable Long id) {
        try {
            Resume resume = resumeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("No resume by id: " + id));
            return ResponseEntity.ok(new ApiResponseDto("Successfully created Resume!", true, resume));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal Server Error",
                    false,
                    e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateResume(@PathVariable Long id, @RequestBody UpdateResumeDto dto) {
        try {
            return ResponseEntity.ok(new ApiResponseDto(
                    "Successfully updated Resume!",
                    true,
                    resumeService.updateResume(id, dto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal Server Error",
                    false,
                    e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteResume(@PathVariable Long id) {
        try {
            resumeService.deleteResume(id);
            return ResponseEntity.ok(new ApiResponseDto("Successfully deleted Resume!", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal Server Error",
                    false,
                    e.getMessage()));
        }
    }

}
