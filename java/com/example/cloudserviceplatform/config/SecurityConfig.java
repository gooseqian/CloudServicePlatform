package com.example.cloudserviceplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
// 暂时注释掉，直到添加必要的依赖
import com.example.cloudserviceplatform.service.UserService;
import com.example.cloudserviceplatform.config.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    
    // 定义UserDetailsService bean，使用MyBatis服务（注释掉以避免重复bean）
    /*
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> userService.loadUserByUsername(username);
    }
    */
    
    // BCrypt密码编码器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 认证提供者（注释掉以避免配置冲突）
    /*
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    */
    
    // 认证管理器
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    // 安全过滤器链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // 允许公共访问的路径
                    .requestMatchers("/login", "/view/register", "/api/auth/register", "/api/auth/custom-login", "/css/**", "/js/**").permitAll()
                    // 其他路径需要认证
                    .anyRequest().authenticated()
            )
            // 禁用默认的表单登录，使用自定义登录处理
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/api/auth/custom-login") // 指定自定义登录处理路径
                    .successHandler(customAuthenticationSuccessHandler) // 使用自定义认证成功处理器
                    .failureUrl("/login?error=true")
                    .permitAll()
            )
            // 登出配置
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            )
            // 简化配置
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions().disable());
            
            return http.build();
    }
    // Spring Security方言配置（确保Thymeleaf中可以使用sec:标签）
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}