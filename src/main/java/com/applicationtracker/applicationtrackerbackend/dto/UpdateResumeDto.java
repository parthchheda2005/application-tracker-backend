package com.applicationtracker.applicationtrackerbackend.dto;

public class UpdateResumeDto {
    String name;
    String firebaseFilePath;

    public UpdateResumeDto(String name, String firebaseFilePath) {
        this.name = name;
        this.firebaseFilePath = firebaseFilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirebaseFilePath() {
        return firebaseFilePath;
    }

    public void setFirebaseFilePath(String firebaseFilePath) {
        this.firebaseFilePath = firebaseFilePath;
    }
}
