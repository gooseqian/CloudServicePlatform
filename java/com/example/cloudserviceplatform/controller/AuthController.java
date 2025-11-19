package com.example.cloudserviceplatform.controller;

import com.example.cloudserviceplatform.dto.RegisterRequest;
import com.example.cloudserviceplatform.vo.User;
import com.example.cloudserviceplatform.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    

    
    // 登录表单提交处理 - 使用Spring Security默认参数名
    @PostMapping("/custom-login")
    public String login(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       HttpServletRequest request,
                       Model model) {
        System.out.println("收到登录请求 - 用户名: " + username);
        
        // 首先检查用户是否存在
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (!userOpt.isPresent()) {
            model.addAttribute("error", "用户不存在！");
            return "login";
        }
        
        try {
            // 验证用户凭证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            
            // 认证成功（如果认证失败会抛出异常）
            // 设置认证信息到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 创建会话并设置必要的会话属性
            HttpSession session = request.getSession(true);
            session.setAttribute("authenticated", true);
            
            // 从userService获取用户详细信息
            User user = userOpt.get();
            session.setAttribute("username", user.getUsername());
            // 基于roles表中的role_code判断是否为管理员
            session.setAttribute("isAdmin", userService.isAdmin(user.getId()));
            
            System.out.println("登录成功，重定向到/home");
            // 登录成功后重定向到home路径，由HomeController根据权限决定最终页面
            return "redirect:/home";
        } catch (BadCredentialsException e) {
            // 密码错误
            
            // 如果用户存在，更新登录尝试次数
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                int attempts = user.getLoginAttempts() != null ? user.getLoginAttempts() : 0;
                attempts++;
                user.setLoginAttempts(attempts);
                
                // 可以考虑在多次失败后锁定账户
                if (attempts >= 5) {
                    user.setIsLocked(true);
                }
            }
            
            model.addAttribute("error", "用户名或密码错误，请重试！");
            return "login";
        } catch (DisabledException e) {
            // 用户账户被禁用
            model.addAttribute("error", "用户账户已被禁用，请联系管理员！");
            return "login";
        } catch (LockedException e) {
            // 用户账户被锁定
            model.addAttribute("error", "用户账户已被锁定，请联系管理员！");
            return "login";
        } catch (Exception e) {
            // 其他登录失败
            model.addAttribute("error", "登录失败，请重试！");
            
            // 正常返回登录页面，显示错误信息
            return "login";
        }
    }
    
    // 注册表单提交处理
    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();
        
        // 密码一致性检查
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            response.put("success", false);
            response.put("message", "两次输入的密码不一致");
            return response;
        }
        
        try {
            // 注册用户 - 直接传递字段值
            String username = registerRequest.getUsername();
            String email = registerRequest.getEmail();
            String password = registerRequest.getPassword();
            String realName = registerRequest.getRealName();
            String phoneNumber = registerRequest.getPhoneNumber();
            String company = registerRequest.getCompany();
            String idCardNo = registerRequest.getIdCard();
            
            // 调用服务层进行注册 - 所有用户自动分配USER角色
            userService.registerUser(username, email, password, realName, phoneNumber, company, idCardNo);
            
            // 注册成功
            response.put("success", true);
            response.put("message", "注册成功，请登录！");
            return response;
        } catch (RuntimeException e) {
            // 注册失败，显示错误信息
            response.put("success", false);
            response.put("message", e.getMessage());
            return response;
        }
    }
    
    // 退出登录处理
    @PostMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取当前会话
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                // 清除会话属性
                session.removeAttribute("authenticated");
                session.removeAttribute("username");
                session.removeAttribute("isAdmin");
                
                // 使会话无效
                session.invalidate();
            }
            
            // 清除安全上下文
            SecurityContextHolder.clearContext();
            
            response.put("success", true);
            response.put("message", "退出登录成功");
            
            System.out.println("用户退出登录成功");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "退出登录失败");
            System.err.println("退出登录异常: " + e.getMessage());
        }
        
        return response;
    }

}