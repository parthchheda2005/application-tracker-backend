package com.applicationtracker.applicationtrackerbackend.dto;

public class CreateApplicationRequestDto {
    private String company;
    private String position;
    private Long resumeId;

    public CreateApplicationRequestDto(String company, String position, Long userId, Long resumeId) {
        this.company = company;
        this.position = position;
        this.resumeId = resumeId;
    }

    public CreateApplicationRequestDto() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }
}
