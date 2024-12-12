package com.portal.service;
public class LoginResponse {
    private boolean success;
    private String message;
    private String userRole;
    private Long userId; // New field for userId

    public LoginResponse(boolean success, String message, String userRole, Long userId) {
        this.success = success;
        this.message = message;
        this.userRole = userRole;
        this.userId = userId;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
