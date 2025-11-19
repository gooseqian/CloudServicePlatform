package com.example.cloudserviceplatform.vo;

import java.time.LocalDateTime;

public class Role {
    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private Boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 构造函数
    public Role() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Role(String roleName, String roleCode) {
        this();
        this.roleName = roleName;
        this.roleCode = roleCode;
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    
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