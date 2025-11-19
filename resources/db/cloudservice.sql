/*
 Navicat Premium Dump SQL

 Source Server         : java
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : cloudservice

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 10/11/2025 14:12:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `permission_code`(`permission_code` ASC) USING BTREE,
  INDEX `idx_permission_code`(`permission_code` ASC) USING BTREE,
  INDEX `idx_resource_type`(`resource_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES (1, '仪表盘访问', 'DASHBOARD_ACCESS', 'SYSTEM', '访问仪表盘的权限');
INSERT INTO `permissions` VALUES (2, '用户管理', 'USER_MANAGE', 'USER', '管理用户账户的权限');
INSERT INTO `permissions` VALUES (3, '角色管理', 'ROLE_MANAGE', 'ROLE', '管理角色和权限的权限');
INSERT INTO `permissions` VALUES (4, '资源管理', 'RESOURCE_MANAGE', 'RESOURCE', '管理云资源的权限');
INSERT INTO `permissions` VALUES (5, '服务管理', 'SERVICE_MANAGE', 'SERVICE', '管理云服务的权限');
INSERT INTO `permissions` VALUES (6, '订单管理', 'ORDER_MANAGE', 'ORDER', '管理服务订单的权限');
INSERT INTO `permissions` VALUES (7, '需求管理', 'DEMAND_MANAGE', 'DEMAND', '管理需求的权限');
INSERT INTO `permissions` VALUES (8, '消息管理', 'MESSAGE_MANAGE', 'MESSAGE', '管理消息的权限');
INSERT INTO `permissions` VALUES (9, '系统设置', 'SYSTEM_SETTING', 'SYSTEM', '管理系统设置的权限');

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions`  (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_permission_id`(`permission_id` ASC) USING BTREE,
  CONSTRAINT `role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permissions
-- ----------------------------
INSERT INTO `role_permissions` VALUES (1, 1);
INSERT INTO `role_permissions` VALUES (1, 2);
INSERT INTO `role_permissions` VALUES (1, 3);
INSERT INTO `role_permissions` VALUES (1, 4);
INSERT INTO `role_permissions` VALUES (1, 5);
INSERT INTO `role_permissions` VALUES (1, 6);
INSERT INTO `role_permissions` VALUES (1, 7);
INSERT INTO `role_permissions` VALUES (1, 8);
INSERT INTO `role_permissions` VALUES (1, 9);
INSERT INTO `role_permissions` VALUES (2, 1);
INSERT INTO `role_permissions` VALUES (2, 4);
INSERT INTO `role_permissions` VALUES (2, 5);
INSERT INTO `role_permissions` VALUES (2, 6);
INSERT INTO `role_permissions` VALUES (2, 7);
INSERT INTO `role_permissions` VALUES (2, 8);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_name`(`role_name` ASC) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_role_name`(`role_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, '管理员', 'ROLE_ADMIN', '系统管理员，拥有所有管理权限');
INSERT INTO `roles` VALUES (2, '用户', 'ROLE_USER', '普通用户，拥有服务发布、购买和资源管理权限');

-- ----------------------------
-- Table structure for user_profiles
-- ----------------------------
DROP TABLE IF EXISTS `user_profiles`;
CREATE TABLE `user_profiles`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `user_role` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_card_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `company_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `business_license_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `total_transactions` int NULL DEFAULT 0,
  `success_rate` decimal(5, 2) NULL DEFAULT 0.00,
  `avg_response_time` decimal(10, 2) NULL DEFAULT 0.00,
  `credit_level` int NULL DEFAULT 0,
  `specialization` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `equipment_capability` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `service_scope` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `certifications` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_user_role`(`user_role` ASC) USING BTREE,
  INDEX `idx_credit_level`(`credit_level` ASC) USING BTREE,
  INDEX `idx_company_name`(`company_name` ASC) USING BTREE,
  CONSTRAINT `user_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_profiles
-- ----------------------------
INSERT INTO `user_profiles` VALUES (4, 4, 'USER', NULL, '巢湖学院', NULL, NULL, 0, 0.00, 0.00, 80, '', '', '', '', '2025-11-08 20:48:52', '2025-11-08 20:48:52');
INSERT INTO `user_profiles` VALUES (6, 6, 'ADMIN', NULL, 'i组织', NULL, NULL, 0, 0.00, 0.00, 80, '', '', '', '', '2025-11-10 08:49:25', '2025-11-10 08:49:25');
INSERT INTO `user_profiles` VALUES (7, 7, 'USER', '34082720050111694', 'user2公司', '巢湖学院', '15664610516', 0, 0.00, 0.00, 80, '人工智能2', '飘', '梨花', '无', '2025-11-10 08:57:34', '2025-11-10 11:45:03');
INSERT INTO `user_profiles` VALUES (10, 10, 'USER', '340827200501116916', '巢湖学院', NULL, NULL, 0, 0.00, 0.00, 80, '', '', '', '', '2025-11-10 11:14:55', '2025-11-10 11:14:55');

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
INSERT INTO `user_roles` VALUES (4, 2);
INSERT INTO `user_roles` VALUES (6, 1);
INSERT INTO `user_roles` VALUES (7, 2);
INSERT INTO `user_roles` VALUES (10, 2);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_active` tinyint(1) NULL DEFAULT 1,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE,
  INDEX `idx_user_type`(`user_type` ASC) USING BTREE,
  INDEX `idx_is_active`(`is_active` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (4, 'user1', '$2a$10$pH44Zz7QhsrFogSd/wld1uVtK5DHh6DKb2/wjADMybFj.aCQgeYv2', '21cerb@qq.com', 'USER', '钱立涵', '15955601084', '/avatars/ee28f904-dcb8-436b-b59a-7a1fcb0689fe.png', 1, '2025-11-08 20:48:52', '2025-11-10 10:59:07');
INSERT INTO `users` VALUES (6, 'admin1', '$2a$10$Am/gVpj.J8TUIIDgB8T4Fe79hXij3TUVJQKTiabA/GX1/uh.E42iG', 'admin1@qq.com', 'ADMIN', 'admin1', '13866609359', NULL, 1, '2025-11-10 08:49:25', '2025-11-10 08:49:25');
INSERT INTO `users` VALUES (7, 'user2', '$2a$10$9efv./uE19PbvGFBjEP0oOdGL5KADxan39P9nCmSG7hGsiTGCRL5G', 'user2@qq.com', 'USER', 'user2', '19966471895', '/avatars/f4de7662-69a7-4135-bbb7-430354352972.jpg', 1, '2025-11-10 08:57:34', '2025-11-10 10:58:49');
INSERT INTO `users` VALUES (10, 'user3', '$2a$10$FSgnlrGB1DaxLuPwokXyUe3P8Y5iBeQevhUx97ELvtDEqCBPKJuHu', 'user3@qq.com', 'USER', '程颐', '13866974521', NULL, 1, '2025-11-10 11:14:55', '2025-11-10 11:14:55');

SET FOREIGN_KEY_CHECKS = 1;
