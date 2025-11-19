package com.example.cloudserviceplatform.controller;

import com.example.cloudserviceplatform.dto.UpdateUserProfileRequest;
import com.example.cloudserviceplatform.service.UserService;
import com.example.cloudserviceplatform.vo.User;
import com.example.cloudserviceplatform.vo.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// 移除错误的导入
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class WebController {
    
    // 登录页面
    @GetMapping("/login")
    public String login() {
        // 如果已登录，重定向到home页面
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return "redirect:/home";
        }
        return "login";
    }
    
    // 注册页面
    @GetMapping("/view/register")
    public String register() {
        // 如果已登录，重定向到home页面
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return "redirect:/home";
        }
        return "register";
    }
    
    // 主页
    @GetMapping("/home")
    public String home(HttpSession session) {
        // 确保用户已登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return "redirect:/login";
        }
        
        // 直接通过Authentication获取用户名
        String username = authentication.getName();
        
        // 从session中获取isAdmin属性，这是在CustomAuthenticationSuccessHandler中设置的
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        // 添加日志输出，用于调试
        System.out.println("User: " + username + ", isAdmin: " + isAdmin);
        
        if (isAdmin != null && isAdmin) {
            // 管理员用户返回adminhome页面
            return "adminhome";
        } else {
            // 普通用户返回默认home页面
            return "home";
        }
    }
    
    // 我的购买页面
    @GetMapping("/mypurchases")
    public String myPurchases() {
        // 确保用户已登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return "redirect:/login";
        }
        return "mypurchases";
    }
    
    // 我的订单页面
    @GetMapping("/myorder")
    public String myOrder() {
        // 确保用户已登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return "redirect:/login";
        }
        return "myorder";
    }
    
    @Autowired
    private UserService userService;
    
    // 个人中心页面
    @GetMapping("/user_profile")
    public String userProfile(Model model) {
        // 确保用户已登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return "redirect:/login";
        }
        
        // 获取当前登录用户名
        String username = authentication.getName();
        
        // 获取用户信息
        User user = userService.findByUsername(username).orElse(null);
        if (user != null) {
            // 获取用户个人资料
            UserProfile userProfile = userService.getUserProfile(user.getId());
            
            // 添加用户基本信息到模型
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("phone", user.getPhoneNumber());
            model.addAttribute("createdTime", user.getCreatedAt() != null ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            model.addAttribute("realName", user.getRealName());
            
            // 添加身份证号到模型
            if (userProfile != null && userProfile.getIdCardNo() != null) {
                model.addAttribute("idCardNo", userProfile.getIdCardNo());
            } else {
                model.addAttribute("idCardNo", "");
            }
            
            // 添加头像URL到模型
            String avatarUrl = user.getAvatarUrl();
            if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
                // 使用默认头像
                avatarUrl = "/images/default-avatar.png";
            }
            model.addAttribute("avatarUrl", avatarUrl);
            
            // 添加用户角色信息
            if (userService.isAdmin(user.getId())) {
                model.addAttribute("userRole", "管理员");
            } else {
                model.addAttribute("userRole", "普通用户");
            }
            
            // 根据数据库user_profiles表结构添加用户详细资料到模型
            // 基本信息
            model.addAttribute("idCardNo", userProfile != null && userProfile.getIdCardNo() != null ? userProfile.getIdCardNo() : "未设置");
            model.addAttribute("companyName", userProfile != null && userProfile.getCompanyName() != null ? userProfile.getCompanyName() : "未设置");
            model.addAttribute("companyAddress", userProfile != null && userProfile.getCompanyAddress() != null ? userProfile.getCompanyAddress() : "未设置");
            model.addAttribute("businessLicenseNo", userProfile != null && userProfile.getBusinessLicenseNo() != null ? userProfile.getBusinessLicenseNo() : "未设置");
            
            // 交易统计信息
            model.addAttribute("totalTransactions", userProfile != null && userProfile.getTotalTransactions() != null ? userProfile.getTotalTransactions() : 0);
            model.addAttribute("successRate", userProfile != null && userProfile.getSuccessRate() != null ? userProfile.getSuccessRate() : "0.00%");
            model.addAttribute("avgResponseTime", userProfile != null && userProfile.getAvgResponseTime() != null ? userProfile.getAvgResponseTime() + "秒" : "0秒");
            
            // 信用和专业信息
            model.addAttribute("creditLevel", userProfile != null && userProfile.getCreditLevel() != null ? userProfile.getCreditLevel() : 0);
            model.addAttribute("specialization", userProfile != null && userProfile.getSpecialization() != null ? userProfile.getSpecialization() : "未设置");
            model.addAttribute("equipmentCapability", userProfile != null && userProfile.getEquipmentCapability() != null ? userProfile.getEquipmentCapability() : "未设置");
            model.addAttribute("serviceScope", userProfile != null && userProfile.getServiceScope() != null ? userProfile.getServiceScope() : "未设置");
            model.addAttribute("certifications", userProfile != null && userProfile.getCertifications() != null ? userProfile.getCertifications() : "未设置");
            
            // 时间信息
            model.addAttribute("profileCreatedAt", userProfile != null && userProfile.getCreatedAt() != null ? userProfile.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "未设置");
            model.addAttribute("profileUpdatedAt", userProfile != null && userProfile.getUpdatedAt() != null ? userProfile.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "未设置");
        }
        
        return "user_profile";
    }
    
    // 更新用户资料
    @PostMapping("/user_profile/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserProfileRequest request) {
        try {
            // 确保用户已登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
                return ResponseEntity.status(401).body("未授权访问");
            }
            
            // 获取当前登录用户
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.status(404).body("用户不存在");
            }
            
            // 更新用户资料
            userService.updateUserProfile(user.getId(), request);
            
            return ResponseEntity.ok().body("资料更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("更新失败: " + e.getMessage());
        }
    }
    
    // 获取用户头像信息
    @GetMapping("/api/user/avatar")
    public ResponseEntity<?> getUserAvatar() {
        try {
            // 确保用户已登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
                // 用户未登录，返回默认头像
                Map<String, String> response = new HashMap<>();
                response.put("avatarUrl", "/images/default-avatar.png");
                return ResponseEntity.ok().body(response);
            }
            
            // 获取当前登录用户
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                Map<String, String> response = new HashMap<>();
                response.put("avatarUrl", "/images/default-avatar.png");
                return ResponseEntity.ok().body(response);
            }
            
            // 获取用户头像URL
            String avatarUrl = user.getAvatarUrl();
            if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
                // 使用默认头像
                avatarUrl = "/images/default-avatar.png";
            }
            
            // 返回头像URL
            Map<String, String> response = new HashMap<>();
            response.put("avatarUrl", avatarUrl);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 出错时返回默认头像
            Map<String, String> response = new HashMap<>();
            response.put("avatarUrl", "/images/default-avatar.png");
            return ResponseEntity.ok().body(response);
        }
    }
    
    // 上传用户头像
    @PostMapping("/user_profile/upload_avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        try {
            // 确保用户已登录
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
                return ResponseEntity.status(401).body("未授权访问");
            }
            
            // 获取当前登录用户
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.status(404).body("用户不存在");
            }
            
            // 验证文件类型
            if (file.isEmpty()) {
                return ResponseEntity.status(400).body("请选择要上传的文件");
            }
            
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(400).body("只能上传图片文件");
            }
            
            // 验证文件大小（最大5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.status(400).body("文件大小不能超过5MB");
            }
            
            // 创建头像存储目录
            String avatarsDir = "src/main/resources/static/avatars";
            File directory = new File(avatarsDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            // 保存文件
            Path filePath = Paths.get(avatarsDir, uniqueFileName);
            Files.copy(file.getInputStream(), filePath);
            
            // 生成头像URL
            String avatarUrl = "/avatars/" + uniqueFileName;
            
            // 更新用户头像URL
            userService.updateUserAvatar(user.getId(), avatarUrl);
            
            return ResponseEntity.ok().body("头像上传成功");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("文件保存失败: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("上传失败: " + e.getMessage());
        }
    }
}