package com.example.cloudserviceplatform.dto;

import java.util.List;

public class RegisterRequest {
    // 基本信息
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    
    // 详细资料
    private String realName;
    private String phoneNumber;
    private String company;
    private String idCard;
    private List<String> businessTypes;
    
    // 用户角色不再由用户选择，注册时自动分配为USER角色
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public String getIdCard() {
        return idCard;
    }
    
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    
    public List<String> getBusinessTypes() {
        return businessTypes;
    }
    
    public void setBusinessTypes(List<String> businessTypes) {
        this.businessTypes = businessTypes;
    }
}