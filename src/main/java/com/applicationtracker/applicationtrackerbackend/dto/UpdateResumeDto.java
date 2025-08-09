package com.applicationtracker.applicationtrackerbackend.dto;

public class UpdateResumeDto {
    String name;
    String azureBlobPath;

    public UpdateResumeDto(String name, String azureBlobPath) {
        this.name = name;
        this.azureBlobPath = azureBlobPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAzureBlobPath() {
        return azureBlobPath;
    }

    public void setAzureBlobPath(String azureBlobPath) {
        this.azureBlobPath = azureBlobPath;
    }
}
