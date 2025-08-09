package com.applicationtracker.applicationtrackerbackend.dto;

public class CreateResumeDto {

    String name;
    String azureBlobPath;

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
