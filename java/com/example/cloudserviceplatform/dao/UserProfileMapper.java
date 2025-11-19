package com.example.cloudserviceplatform.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.cloudserviceplatform.vo.UserProfile;
import java.util.Optional;

@Mapper
public interface UserProfileMapper {
    // 插入用户档案
    void insert(UserProfile userProfile);
    
    // 根据ID查询用户档案
    UserProfile selectById(@Param("id") Long id);
    
    // 根据用户ID查询用户档案
    Optional<UserProfile> selectByUserId(@Param("userId") Long userId);
    
    // 更新用户档案
    void update(UserProfile userProfile);
    
    // 删除用户档案
    void delete(@Param("id") Long id);
}