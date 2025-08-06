package com.applicationtracker.applicationtrackerbackend.dto;

import com.applicationtracker.applicationtrackerbackend.model.Application;

import java.util.List;

public class GetApplicationsDto {
    private long totalElements;
    private int totalPages;
    List<Application> applications;

    public GetApplicationsDto() {
    }

    public GetApplicationsDto(long totalElements, int totalPages, List<Application> applications) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.applications = applications;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
