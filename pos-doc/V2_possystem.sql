-- ==============================
-- Improved database scripts are moved here
-- ==============================

-- 使用现有数据库
USE pos_db;

-- 设置默认存储引擎和字符集
SET default_storage_engine = InnoDB;
SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `merchants` (
    `merchant_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家ID UUID',
    `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家邮箱（唯一）',
    `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家手机号（唯一）',
    `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户名称',
    `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
    `business_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业地址',
    `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '商家状态',
    `last_login_at` bigint unsigned DEFAULT NULL COMMENT '最后登录时间',
    `created_at` bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at` bigint unsigned DEFAULT NULL COMMENT '更新时间',
    `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`merchant_id`) USING BTREE,
    KEY `business_name` (`business_name`),
    KEY `status` (`status`),
    KEY `is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家表';

CREATE TABLE `menus` (
    `menu_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单ID UUID',
    `merchant_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家ID',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '菜单描述',
    `start_time` time DEFAULT NULL COMMENT '适用开始时间',
    `end_time` time DEFAULT NULL COMMENT '适用结束时间',
    `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '启用状态（1启用，0停用）',
    `created_at` bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at` bigint unsigned DEFAULT NULL COMMENT '更新时间',
    `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`menu_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `is_active` (`is_active`),
    KEY `is_deleted` (`is_deleted`),
    CONSTRAINT `fk_menus_merchant_id` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`merchant_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

CREATE TABLE `menu_categories` (
    `category_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目ID UUID',
    `merchant_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家ID',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类目描述',
    `display_order` int DEFAULT '0' COMMENT '显示顺序',
    `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '启用状态（1启用，0停用）',
    `created_at` bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at` bigint unsigned DEFAULT NULL COMMENT '更新时间',
    `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`category_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `is_active` (`is_active`),
    KEY `is_deleted` (`is_deleted`),
    CONSTRAINT `fk_menu_categories_merchant_id` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`merchant_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单类目表';

CREATE TABLE `items` (
    `item_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品ID UUID',
    `merchant_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家ID',
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '菜品描述',
    `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '基础价格',
    `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片地址',
    `is_combo` tinyint NOT NULL DEFAULT '0' COMMENT '是否为套餐（1是，0否）',
    `combo_items` json DEFAULT NULL COMMENT '套餐组成（JSON格式，[{item_id, quantity}]）',
    `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'AVAILABLE' COMMENT '菜品状态',
    `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '上架状态（1上架，0下架）',
    `created_at` bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at` bigint unsigned DEFAULT NULL COMMENT '更新时间',
    `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`item_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `status` (`status`),
    KEY `is_active` (`is_active`),
    KEY `is_deleted` (`is_deleted`),
    CONSTRAINT `fk_items_merchant_id` FOREIGN KEY (`merchant_id`) REFERENCES `merchants` (`merchant_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜品表（支持套餐）';

CREATE TABLE `menu_category_items` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `menu_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单ID',
    `category_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目ID',
    `item_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品ID',
    `display_order` int DEFAULT '0' COMMENT '显示顺序',
    `created_at` bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at` bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `unique_relation` (`menu_id`, `category_id`, `item_id`),
    KEY `menu_id` (`menu_id`),
    KEY `category_id` (`category_id`),
    KEY `item_id` (`item_id`),
    CONSTRAINT `fk_mci_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`menu_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_mci_category_id` FOREIGN KEY (`category_id`) REFERENCES `menu_categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_mci_item_id` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单-类目-菜品关联表';
