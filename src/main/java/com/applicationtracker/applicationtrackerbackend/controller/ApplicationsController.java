package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.ApiResponseDto;
import com.applicationtracker.applicationtrackerbackend.dto.CreateApplicationRequestDto;
import com.applicationtracker.applicationtrackerbackend.dto.GetApplicationsDto;
import com.applicationtracker.applicationtrackerbackend.model.Application;
import com.applicationtracker.applicationtrackerbackend.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
public class ApplicationsController {

    private ApplicationService applicationService;

    public ApplicationsController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponseDto> createApplication(@RequestBody CreateApplicationRequestDto req) {
        try {
            Application app = applicationService.createApplication(req);
            ApiResponseDto apiResponseDto = new ApiResponseDto("Successfully created application!", true, app);
            return ResponseEntity.ok(apiResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false));
        }
    }

    @GetMapping("/all-applications")
    public ResponseEntity<ApiResponseDto> getApplications(@RequestParam int page,
                                                          @RequestParam int pageSize,
                                                          @RequestParam String sortBy) {

        try {
            GetApplicationsDto getApplicationsDto = applicationService.getApplications(page, pageSize, sortBy);
            return ResponseEntity.ok(new ApiResponseDto(
                    "Successfully retrieved applications",
                    true,
                    getApplicationsDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false));
        }

    }

}
