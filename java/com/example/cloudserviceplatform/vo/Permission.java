package com.example.cloudserviceplatform.vo;

import java.time.LocalDateTime;

public class Permission {
    private Long id;
    private String permissionName;
    private String permissionCode;
    private String resourceType;
    private String resourcePath;
    private String action;
    private String description;
    private Boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 构造函数
    public Permission() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Permission(String permissionName, String permissionCode) {
        this();
        this.permissionName = permissionName;
        this.permissionCode = permissionCode;
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    
    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public String getResourcePath() { return resourcePath; }
    public void setResourcePath(String resourcePath) { this.resourcePath = resourcePath; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}