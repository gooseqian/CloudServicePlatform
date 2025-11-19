package com.example.cloudserviceplatform.util;

import com.example.cloudserviceplatform.vo.User;
import com.example.cloudserviceplatform.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AdminPasswordFixer {

    private static final Logger logger = LoggerFactory.getLogger(AdminPasswordFixer.class);
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Transactional
    public void fixAdminPassword() {
        try {
            // 查找管理员用户
            User admin = userMapper.selectByUsername("admin")
                    .orElseThrow(() -> new RuntimeException("管理员用户不存在"));
            
            logger.info("找到管理员用户: {}", admin.getUsername());
            
            // 编码新密码
            String rawPassword = "admin123";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            
            // 更新密码
            admin.setPassword(encodedPassword);
            userMapper.update(admin);
            
            logger.info("管理员密码更新成功");
            
        } catch (Exception e) {
            logger.error("更新管理员密码失败", e);
        }
    }
    
    // 用于测试的主方法
    // main方法已移除，此工具类应通过Spring容器调用
}