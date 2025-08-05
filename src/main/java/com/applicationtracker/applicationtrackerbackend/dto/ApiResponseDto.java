package com.applicationtracker.applicationtrackerbackend.dto;

public class ApiResponseDto {
    private String message;
    private boolean success;
    private Object data;

    public ApiResponseDto(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public ApiResponseDto(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setObject(Object data) {
        this.data = data;
    }
}
