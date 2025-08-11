package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.DateCountDto;
import com.applicationtracker.applicationtrackerbackend.dto.ResumeCountDto;
import com.applicationtracker.applicationtrackerbackend.dto.ResumeRatingResponseDto;
import com.applicationtracker.applicationtrackerbackend.dto.StatusCountDto;
import com.applicationtracker.applicationtrackerbackend.service.ApplicationService;
import com.applicationtracker.applicationtrackerbackend.service.GeminiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final ApplicationService applicationService;
    private final GeminiService geminiService;

    public AnalyticsController(ApplicationService applicationService, GeminiService geminiService) {
        this.applicationService = applicationService;
        this.geminiService = geminiService;
    }

    @GetMapping("/last-7-days")
    public List<DateCountDto> getApplicationsStatsLast7Days() {
        return applicationService.getApplicationsCountLast7Days();
    }

    @GetMapping("/resume-interview-offer")
    public List<ResumeCountDto> getResumeNameStatsInterviewOffer() {
        return applicationService.getResumeNameCountsForInterviewOrOffer();
    }

    @GetMapping("/application-by-status")
    public List<StatusCountDto> getApplicationsByStatus() {
        return applicationService.getApplicationsByStatus();
    }

    @GetMapping("/resume-rating")
    public ResumeRatingResponseDto getResumeRating() {
       return geminiService.getResumeRating();
    }
}
