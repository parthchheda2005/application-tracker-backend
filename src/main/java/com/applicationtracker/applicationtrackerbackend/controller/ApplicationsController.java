package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.ApiResponseDto;
import com.applicationtracker.applicationtrackerbackend.dto.GetApplicationsDto;
import com.applicationtracker.applicationtrackerbackend.dto.SaveApplicationDto;
import com.applicationtracker.applicationtrackerbackend.dto.UpdateStatusDto;
import com.applicationtracker.applicationtrackerbackend.model.Application;
import com.applicationtracker.applicationtrackerbackend.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
public class ApplicationsController {

    private final ApplicationService applicationService;

    public ApplicationsController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createApplication(@RequestBody SaveApplicationDto req) {
        try {
            Application app = applicationService.createApplication(req);
            ApiResponseDto apiResponseDto = new ApiResponseDto("Successfully created application!", true, app);
            return ResponseEntity.ok(apiResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false,
                    e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getApplications(@RequestParam int page,
                                                          @RequestParam int pageSize,
                                                          @RequestParam String sortBy) {

        try {
            if (page == 0) throw new RuntimeException("Page must be > 0");

            GetApplicationsDto getApplicationsDto = applicationService.getApplications(page - 1, pageSize, sortBy);
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getApplication(@PathVariable Long id) {
        try {
            Application application = applicationService.getApplicationById(id);
            return ResponseEntity.ok(new ApiResponseDto(
                    "Successfully retrieved applications",
                    true,
                    application));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false));
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<ApiResponseDto> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusDto req) {
        try {
            Application updatedApplication = applicationService.updateStatus(id, req.getStatus());
            return ResponseEntity.ok(new ApiResponseDto(
                    "Successfully updated application status",
                    true,
                    updatedApplication));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false,
                    e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateApplication(@PathVariable Long id,
                                                            @RequestBody SaveApplicationDto req) {
        try {
            Application updatedApplication = applicationService.updateApplication(id, req);
            return ResponseEntity.ok(new ApiResponseDto(
                    "Successfully updated application",
                    true,
                    updatedApplication));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            return ResponseEntity.ok(new ApiResponseDto("Successfully deleted application", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(
                    "Internal server error",
                    false));
        }
    }

}
