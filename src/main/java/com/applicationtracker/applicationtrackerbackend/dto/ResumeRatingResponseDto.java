package com.applicationtracker.applicationtrackerbackend.dto;

import java.util.List;

public class ResumeRatingResponseDto {
    private int rating;
    private List<String> pros;
    private List<String> improvements;

    public ResumeRatingResponseDto() {
    }

    public ResumeRatingResponseDto(int rating, List<String> pros, List<String> improvements) {
        this.rating = rating;
        this.pros = pros;
        this.improvements = improvements;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getPros() {
        return pros;
    }

    public void setPros(List<String> pros) {
        this.pros = pros;
    }

    public List<String> getImprovements() {
        return improvements;
    }

    public void setImprovements(List<String> improvements) {
        this.improvements = improvements;
    }

    @Override
    public String toString() {
        return "ResumeRatingResponse{" +
                "rating=" + rating +
                ", pros=" + pros +
                ", improvements=" + improvements +
                '}';
    }
}
