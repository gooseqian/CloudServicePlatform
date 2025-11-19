package com.example.cloudserviceplatform.vo;

import java.time.LocalDateTime;

public class UserProfile {
    private Long id;
    private Long userId;
    private UserRole userRole;
    private String idCardNo;
    
    private String realName;
    private String companyName;
    private String companyRegistrationNumber;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private String businessLicenseUrl;
    private Integer creditLevel = 80;
    private Integer totalTransactions = 0;
    private Double successRate = 0.0;
    private Double avgResponseTime = 0.0;
    private String specialization;
    private String equipmentCapability;
    private String serviceScope;
    private String certifications;
    
    // 兼容旧的getter/setter，同时支持新的字段名
    public Integer getCreditScore() {
        return creditLevel;
    }
    
    public void setCreditScore(Integer creditScore) {
        this.creditLevel = creditScore;
    }
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum UserRole {
        USER,
        ADMIN
    }
    
    // 构造函数
    public UserProfile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public UserProfile(Long userId, UserRole userRole) {
        this();
        this.userId = userId;
        this.userRole = userRole;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public UserRole getUserRole() {
        return userRole;
    }
    
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    

    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }
    
    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }
    
    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }
    
    public Integer getCreditLevel() {
        return creditLevel;
    }
    
    public void setCreditLevel(Integer creditLevel) {
        this.creditLevel = creditLevel;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
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
    
    public Integer getTotalTransactions() {
        return totalTransactions;
    }
    
    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
    
    public Double getSuccessRate() {
        return successRate;
    }
    
    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }
    
    public Double getAvgResponseTime() {
        return avgResponseTime;
    }
    
    public void setAvgResponseTime(Double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getEquipmentCapability() {
        return equipmentCapability;
    }
    
    public void setEquipmentCapability(String equipmentCapability) {
        this.equipmentCapability = equipmentCapability;
    }
    
    public String getServiceScope() {
        return serviceScope;
    }
    
    public void setServiceScope(String serviceScope) {
        this.serviceScope = serviceScope;
    }
    
    public String getCertifications() {
        return certifications;
    }
    
    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }
    
    // 兼容数据库字段名的getter/setter方法
    public String getIdCardNo() {
        return this.idCardNo;
    }
    
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
    
    public String getCompanyAddress() {
        return this.address;
    }
    
    public void setCompanyAddress(String companyAddress) {
        this.address = companyAddress;
    }
    
    public String getBusinessLicenseNo() {
        return this.businessLicenseUrl;
    }
    
    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseUrl = businessLicenseNo;
    }
    
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}