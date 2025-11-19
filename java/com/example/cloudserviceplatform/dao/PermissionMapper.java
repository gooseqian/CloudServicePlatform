package com.example.cloudserviceplatform.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import com.example.cloudserviceplatform.vo.Permission;

@Mapper
public interface PermissionMapper {
    // 插入权限
    void insert(Permission permission);
    
    // 根据ID查询权限
    Permission selectById(@Param("id") Long id);
    
    // 根据权限代码查询权限
    Optional<Permission> selectByPermissionCode(@Param("permissionCode") String permissionCode);
    
    // 根据权限名称查询权限
    Optional<Permission> selectByPermissionName(@Param("permissionName") String permissionName);
    
    // 查询所有权限
    List<Permission> selectAll();
    
    // 根据资源类型查询权限列表
    List<Permission> selectByResourceType(@Param("resourceType") String resourceType);
    
    // 更新权限信息
    void update(Permission permission);
    
    // 删除权限
    void delete(@Param("id") Long id);
    
    // 查询角色的权限列表
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
    
    // 查询用户的权限列表
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);
    
    // 为角色分配权限
    void insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
    
    // 移除角色的权限
    void deleteRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}