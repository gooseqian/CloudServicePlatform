package com.example.cloudserviceplatform.config;

import com.example.cloudserviceplatform.vo.User;
import com.example.cloudserviceplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        // 获取用户名
        String username = authentication.getName();
        
        // 从数据库获取用户信息
        User user = userService.findByUsername(username).orElse(null);
        
        // 设置会话属性
        request.getSession().setAttribute("authenticated", true);
        request.getSession().setAttribute("username", username);
        
        // 使用UserService中的isAdmin方法检查用户是否具有管理员角色
        boolean isAdmin = user != null && userService.isAdmin(user.getId());
        request.getSession().setAttribute("isAdmin", isAdmin);
        
        System.out.println("CustomAuthenticationSuccessHandler: 用户 " + username + " 登录成功，是否管理员: " + isAdmin);
        
        // 重定向到home路径，让WebController根据用户权限决定显示哪个页面
        response.sendRedirect("/home");
    }
}