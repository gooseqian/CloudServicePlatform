package com.example.cloudserviceplatform.service;

import com.example.cloudserviceplatform.dto.UpdateUserProfileRequest;
import com.example.cloudserviceplatform.vo.Permission;
import com.example.cloudserviceplatform.vo.Role;
import com.example.cloudserviceplatform.vo.User;
import com.example.cloudserviceplatform.vo.UserProfile;
import com.example.cloudserviceplatform.dao.PermissionMapper;
import com.example.cloudserviceplatform.dao.RoleMapper;
import com.example.cloudserviceplatform.dao.UserMapper;
import com.example.cloudserviceplatform.dao.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private UserProfileMapper userProfileMapper;
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    // 使用构造函数创建编码器实例，避免循环依赖
    private BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private String encodePassword(String rawPassword) {
        return getPasswordEncoder().encode(rawPassword);
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 检查用户名是否存在
            User user = userMapper.selectByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
            
            // 获取用户的角色和权限
            List<GrantedAuthority> authorities = new ArrayList<>();
            List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
            
            if (roles.isEmpty()) {
                // 为没有角色的用户默认添加ROLE_USER角色
                Role defaultRole = roleMapper.selectByRoleCode("ROLE_USER").orElse(null);
                if (defaultRole != null) {
                    roleMapper.insertUserRole(user.getId(), defaultRole.getId());
                    roles.add(defaultRole);
                }
            }
            
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
                // 添加角色的权限
                List<Permission> permissions = permissionMapper.selectPermissionsByRoleId(role.getId());
                for (Permission permission : permissions) {
                    authorities.add(new SimpleGrantedAuthority(permission.getPermissionCode()));
                }
            }
            
            // 创建UserDetails对象
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    true, true, true, // accountNonExpired, credentialsNonExpired, accountNonLocked
                    authorities
            );
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public User registerUser(String username, String email, String password, String realName, 
                            String phoneNumber, String company, String idCardNo) {
        try {
            // 检查用户名和邮箱是否已存在
            if (userMapper.countByUsername(username) > 0) {
                throw new RuntimeException("用户名已存在");
            }
            if (userMapper.countByEmail(email) > 0) {
                throw new RuntimeException("邮箱已被注册");
            }
            
            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(encodePassword(password));
            user.setUserType(User.UserType.USER);
            user.setRealName(realName);
            user.setPhoneNumber(phoneNumber);
            user.setIsActive(true);
            user.setIsLocked(false);
            user.setLoginAttempts(0);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            
            // 保存用户
            userMapper.insert(user);
            
            // 分配角色 - 所有注册用户默认都是ROLE_USER
            final String roleCode = "ROLE_USER";
            final UserProfile.UserRole profileRole = UserProfile.UserRole.USER;
            
            Role role = roleMapper.selectByRoleCode(roleCode)
                    .orElseThrow(() -> new RuntimeException("角色不存在: " + roleCode));
            
            // 关联用户和角色
            roleMapper.insertUserRole(user.getId(), role.getId());
            
            // 创建用户档案
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());
            userProfile.setUserRole(profileRole);
            userProfile.setRealName(realName);
            userProfile.setCompanyName(company);
            userProfile.setIdCardNo(idCardNo);
            userProfile.setCreditLevel(80); // 使用Integer类型，匹配UserProfile类的字段类型
            // 设置与数据库表对应的其他字段
            userProfile.setTotalTransactions(0); // 使用Integer类型，匹配UserProfile类的字段类型
            userProfile.setSuccessRate(0.0); // Double类型
            userProfile.setAvgResponseTime(0.0); // Double类型
            userProfile.setSpecialization("");
            userProfile.setEquipmentCapability("");
            userProfile.setServiceScope("");
            userProfile.setCertifications("");
            userProfile.setCreatedAt(LocalDateTime.now());
            userProfile.setUpdatedAt(LocalDateTime.now());
            
            // 保存用户档案
            userProfileMapper.insert(userProfile);
            
            return user;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Optional<User> findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userMapper.selectByEmail(email);
    }
    
    public User findById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在: " + id);
        }
        return user;
    }
    
    /**
     * 检查用户是否具有管理员角色
     * @param userId 用户ID
     * @return 如果用户具有ROLE_ADMIN角色则返回true
     */
    public boolean isAdmin(Long userId) {
        List<Role> roles = roleMapper.selectRolesByUserId(userId);
        for (Role role : roles) {
            if ("ROLE_ADMIN".equals(role.getRoleCode())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkPassword(User user, String rawPassword) {
        return getPasswordEncoder().matches(rawPassword, user.getPassword());
    }
    
    @Transactional
    public UserProfile getUserProfile(Long userId) {
        return userProfileMapper.selectByUserId(userId).orElse(null);
    }
    
    @Transactional
    public void updateUserProfile(Long userId, UpdateUserProfileRequest request) {
        // 获取现有用户档案
        UserProfile userProfile = userProfileMapper.selectByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户档案不存在"));
        
        // 更新字段
        if (request.getIdCardNo() != null) {
            userProfile.setIdCardNo(request.getIdCardNo());
        }
        if (request.getCompanyName() != null) {
            userProfile.setCompanyName(request.getCompanyName());
        }
        if (request.getCompanyAddress() != null) {
            userProfile.setCompanyAddress(request.getCompanyAddress());
        }
        if (request.getBusinessLicenseNo() != null) {
            userProfile.setBusinessLicenseNo(request.getBusinessLicenseNo());
        }
        if (request.getSpecialization() != null) {
            userProfile.setSpecialization(request.getSpecialization());
        }
        if (request.getEquipmentCapability() != null) {
            userProfile.setEquipmentCapability(request.getEquipmentCapability());
        }
        if (request.getServiceScope() != null) {
            userProfile.setServiceScope(request.getServiceScope());
        }
        if (request.getCertifications() != null) {
            userProfile.setCertifications(request.getCertifications());
        }
        
        // 更新时间戳
        userProfile.setUpdatedAt(LocalDateTime.now());
        
        // 保存更新
        userProfileMapper.update(userProfile);
    }
    
    @Transactional
    public void updateUserAvatar(Long userId, String avatarUrl) {
        // 获取用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新头像URL
        user.setAvatarUrl(avatarUrl);
        user.setUpdatedAt(LocalDateTime.now());
        
        // 保存更新
        userMapper.update(user);
    }
}