package com.example.cloudserviceplatform.dto;

import lombok.Data;

/**
 * 更新用户资料请求DTO
 */
@Data
public class UpdateUserProfileRequest {
    private String idCardNo;         // 身份证号
    private String companyName;      // 公司名称
    private String companyAddress;   // 公司地址
    private String businessLicenseNo; // 营业执照号
    private String specialization;   // 专业领域
    private String equipmentCapability; // 设备能力
    private String serviceScope;     // 服务范围
    private String certifications;   // 认证资质
    
    // 手动添加getter方法以确保编译通过
    public String getIdCardNo() {
        return idCardNo;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public String getCompanyAddress() {
        return companyAddress;
    }
    
    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public String getEquipmentCapability() {
        return equipmentCapability;
    }
    
    public String getServiceScope() {
        return serviceScope;
    }
    
    public String getCertifications() {
        return certifications;
    }
}