package com.example.cloudserviceplatform.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "用户名或邮箱不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;

    // 构造函数
    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter和Setter方法
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}