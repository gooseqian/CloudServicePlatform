package com.example.cloudserviceplatform.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.cloudserviceplatform.vo.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    // 插入用户
    void insert(User user);
    
    // 根据ID查询用户
    User selectById(@Param("id") Long id);
    
    // 根据用户名查询用户
    Optional<User> selectByUsername(@Param("username") String username);
    
    // 根据邮箱查询用户
    Optional<User> selectByEmail(@Param("email") String email);
    
    // 更新用户信息
    void update(User user);
    
    // 查询所有用户
    List<User> selectAll();
    
    // 根据用户名检查是否存在
    int countByUsername(@Param("username") String username);
    
    // 根据邮箱检查是否存在
    int countByEmail(@Param("email") String email);
    
    // 切换用户激活状态
    void toggleActive(@Param("id") Long id);
}