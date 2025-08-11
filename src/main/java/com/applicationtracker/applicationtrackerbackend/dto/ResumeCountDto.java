package com.applicationtracker.applicationtrackerbackend.dto;

public class ResumeCountDto {
    private String resumeName;
    private Long count;

    public ResumeCountDto(String resumeName, Long count) {
        this.resumeName = resumeName;
        this.count = count;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
