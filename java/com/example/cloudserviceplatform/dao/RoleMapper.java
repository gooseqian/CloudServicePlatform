package com.example.cloudserviceplatform.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.cloudserviceplatform.vo.Role;
import java.util.List;
import java.util.Optional;

@Mapper
public interface RoleMapper {
    // 插入角色
    void insert(Role role);
    
    // 根据ID查询角色
    Role selectById(@Param("id") Long id);
    
    // 根据角色代码查询角色
    Optional<Role> selectByRoleCode(@Param("roleCode") String roleCode);
    
    // 根据角色名称查询角色
    Optional<Role> selectByRoleName(@Param("roleName") String roleName);
    
    // 查询所有角色
    List<Role> selectAll();
    
    // 更新角色信息
    void update(Role role);
    
    // 删除角色
    void delete(@Param("id") Long id);
    
    // 查询用户的角色列表
    List<Role> selectRolesByUserId(@Param("userId") Long userId);
    
    // 为用户分配角色
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    // 移除用户的角色
    void deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}