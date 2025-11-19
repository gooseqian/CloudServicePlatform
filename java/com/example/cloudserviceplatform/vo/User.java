package com.example.cloudserviceplatform.vo;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String realName;
    private String phoneNumber;
    private String avatarUrl;
    private Boolean isActive = true;
    private Boolean isLocked = false;
    private Integer loginAttempts = 0;
    private UserType userType = UserType.USER;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    
    public enum UserType {
        ADMIN,
        USER
    }
    
    // 构造函数
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public User(String username, String password, String email) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getIsLocked() {
        return isLocked;
    }
    
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }
    
    public Integer getLoginAttempts() {
        return loginAttempts;
    }
    
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public boolean isEnabled() {
        return isActive != null && isActive && (isLocked == null || !isLocked);
    }
    
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}