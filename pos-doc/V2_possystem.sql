-- ==============================
-- Improved database scripts are moved here
-- ==============================

-- 使用现有数据库
USE pos_db;

-- 设置默认存储引擎和字符集
SET default_storage_engine = InnoDB;
SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `merchants`
(
    `merchant_id`      char(36)     NOT NULL COMMENT '商家ID UUID',
    `email`            varchar(255) NOT NULL COMMENT '商家邮箱（唯一）',
    `phone_number`     varchar(20)  NOT NULL COMMENT '商家手机号（唯一）',
    `password_hash`    varchar(255) NOT NULL COMMENT '密码哈希',
    `name`             varchar(255) NOT NULL COMMENT '商户名称',
    `business_name`    varchar(255) NOT NULL COMMENT '企业名称',
    `business_address` varchar(255) NOT NULL COMMENT '企业地址',
    `status`           varchar(50)  NOT NULL DEFAULT 'ACTIVE' COMMENT '商家状态',
    `last_login_at`    bigint unsigned       DEFAULT NULL COMMENT '最后登录时间',
    `created_at`       bigint unsigned       DEFAULT NULL COMMENT '创建时间',
    `updated_at`       bigint unsigned       DEFAULT NULL COMMENT '更新时间',
    `is_deleted`       tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`merchant_id`) USING BTREE,
    KEY `business_name` (`business_name`),
    KEY `status` (`status`),
    KEY `is_deleted` (`is_deleted`)
) COMMENT ='商家表';

CREATE TABLE `stores`
(
    `id`             char(36)     NOT NULL COMMENT '门店ID',
    `merchant_id`    char(36)     NOT NULL COMMENT '所属商家ID',
    `store_name`     varchar(255) NOT NULL COMMENT '门店名称',
    `address`        varchar(255)    DEFAULT NULL COMMENT '门店地址',
    `timezone`       varchar(64)     DEFAULT 'UTC' COMMENT '时区',
    `status`         varchar(50)     DEFAULT 'ACTIVE' COMMENT '门店状态',
    `tax_rate`       decimal(5, 4)   DEFAULT '0.0000' COMMENT '默认税率',
    `currency`       varchar(3)      DEFAULT 'USD' COMMENT '币种',
    `business_hours` json            DEFAULT NULL COMMENT '营业时间配置',
    `created_at`     bigint unsigned DEFAULT NULL COMMENT '创建时间,毫秒时间戳',
    `updated_at`     bigint unsigned DEFAULT NULL COMMENT '更新时间,毫秒时间戳',
    `is_deleted`     tinyint(1)      DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `merchant_id` (`merchant_id`),
    KEY `store_name` (`store_name`),
    KEY `is_deleted` (`is_deleted`),
    KEY `created_at` (`created_at`),
    KEY `updated_at` (`updated_at`)
) COMMENT ='门店表（UUID主键）';

CREATE TABLE `devices`
(
    `merchant_id`   char(36)     NOT NULL COMMENT '商家ID',
    `device_id`     char(36)     NOT NULL COMMENT '设备主键（UUID）',
    `store_id`      char(36)     NOT NULL COMMENT '所属店铺',
    `device_name`   varchar(100) NOT NULL COMMENT '设备名称',
    `device_type`   varchar(50)  NOT NULL COMMENT '设备类型',
    `mac_address`   varchar(17)           DEFAULT NULL COMMENT 'MAC地址',
    `ip_address`    varchar(15)           DEFAULT NULL COMMENT 'IP地址',
    `last_online`   bigint                DEFAULT NULL COMMENT '最后在线时间',
    `status`        varchar(20)  NOT NULL DEFAULT 'OFFLINE' COMMENT '设备状态',
    `registered_at` bigint                DEFAULT NULL COMMENT '注册时间',
    `last_login_at` bigint                DEFAULT NULL COMMENT '最后一次登录时间',
    `is_deleted`    tinyint(1)            DEFAULT '0' COMMENT '软删除标识',
    PRIMARY KEY (`device_id`),
    KEY `is_deleted` (`is_deleted`) USING BTREE,
    KEY `ip_address` (`ip_address`),
    KEY `mac_address` (`mac_address`),
    KEY `last_login_at` (`last_login_at`),
    KEY `registered_at` (`registered_at`)
) ENGINE = InnoDB COMMENT ='设备表';

CREATE TABLE `employees`
(
    `employees_id`  char(36)     NOT NULL COMMENT '用户ID Square风格',
    `merchant_id`   char(36)     NOT NULL COMMENT '商家ID',
    `store_id`      char(36)     NOT NULL COMMENT '门店ID',
    `email`         varchar(100) NOT NULL COMMENT '邮箱（唯一）',
    `pass_code`     varchar(255)          DEFAULT NULL COMMENT 'PIN码哈希',
    `phone_number`  varchar(20)  NOT NULL COMMENT '手机号（唯一）',
    `first_name`    varchar(50)           DEFAULT NULL COMMENT '名',
    `last_name`     varchar(50)           DEFAULT NULL COMMENT '姓',
    `role_id`       char(36)     NOT NULL COMMENT '角色ID',
    `status`        varchar(50)  NOT NULL DEFAULT 'ACTIVE' COMMENT '账号状态',
    `last_login_at` bigint unsigned       DEFAULT NULL COMMENT '最后登录时间',
    `created_at`    bigint unsigned       DEFAULT NULL,
    `updated_at`    bigint unsigned       DEFAULT NULL,
    `is_deleted`    tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`employees_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `store_id` (`store_id`),
    KEY `email` (`email`),
    KEY `first_name` (`first_name`),
    KEY `last_name` (`last_name`),
    KEY `role_id` (`role_id`),
    KEY `status` (`status`),
    KEY `is_deleted` (`is_deleted`)
) ENGINE = InnoDB COMMENT ='员工表';

CREATE TABLE `attendance`
(
    `merchant_id`    char(36)        NOT NULL COMMENT '商家ID',
    `attendance_id`  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '考勤主键',
    `employees_id`   char(36)        NOT NULL COMMENT '员工ID',
    `store_id`       char(36)        NOT NULL COMMENT '所属店铺',
    `clock_in_time`  bigint unsigned NOT NULL COMMENT '上班打卡时间,毫秒级时间戳',
    `clock_out_time` bigint unsigned DEFAULT NULL COMMENT '下班打卡时间,毫秒级时间戳',
    `total_time`     bigint unsigned DEFAULT NULL COMMENT '工作总时长,=clock_in_time-clock_out_time',
    PRIMARY KEY (`attendance_id`),
    KEY `employees_id` (`employees_id`),
    KEY `store_id` (`store_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3 COMMENT ='考勤表';

CREATE TABLE `menus`
(
    `menu_id`     char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '菜单ID UUID',
    `merchant_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '商家ID',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '菜单描述',
    `start_time`  time                                                                   DEFAULT NULL COMMENT '适用开始时间',
    `end_time`    time                                                                   DEFAULT NULL COMMENT '适用结束时间',
    `is_active`   tinyint                                                       NOT NULL DEFAULT '1' COMMENT '启用状态（1启用，0停用）',
    `created_at`  bigint unsigned                                                        DEFAULT NULL COMMENT '创建时间',
    `updated_at`  bigint unsigned                                                        DEFAULT NULL COMMENT '更新时间',
    `is_deleted`  tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`menu_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `is_active` (`is_active`),
    KEY `is_deleted` (`is_deleted`)
) ENGINE = InnoDB COMMENT ='菜单表';

CREATE TABLE `menu_categories`
(
    `category_id`   char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '类目ID UUID',
    `merchant_id`   char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '商家ID',
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目名称',
    `description`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '类目描述',
    `display_order` int                                                                    DEFAULT '0' COMMENT '显示顺序',
    `is_active`     tinyint                                                       NOT NULL DEFAULT '1' COMMENT '启用状态（1启用，0停用）',
    `created_at`    bigint unsigned                                                        DEFAULT NULL COMMENT '创建时间',
    `updated_at`    bigint unsigned                                                        DEFAULT NULL COMMENT '更新时间',
    `is_deleted`    tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`category_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `is_active` (`is_active`),
    KEY `is_deleted` (`is_deleted`)
) ENGINE = InnoDB COMMENT ='菜单类目表';

CREATE TABLE `items`
(
    `item_id`     char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '菜品ID UUID',
    `merchant_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '商家ID',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '菜品描述',
    `price`       decimal(10, 2)                                                NOT NULL DEFAULT '0.00' COMMENT '基础价格',
    `image_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '图片地址',
    `is_combo`    tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否为套餐（1是，0否）',
    `combo_items` json                                                                   DEFAULT NULL COMMENT '套餐组成（JSON格式，[{item_id, quantity}]）',
    `status`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'AVAILABLE' COMMENT '菜品状态',
    `is_active`   tinyint                                                       NOT NULL DEFAULT '1' COMMENT '上架状态（1上架，0下架）',
    `created_at`  bigint unsigned                                                        DEFAULT NULL COMMENT '创建时间',
    `updated_at`  bigint unsigned                                                        DEFAULT NULL COMMENT '更新时间',
    `is_deleted`  tinyint                                                       NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`item_id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `status` (`status`),
    KEY `is_active` (`is_active`),
    KEY `is_deleted` (`is_deleted`)
) ENGINE = InnoDB COMMENT ='菜品表（支持套餐）';

CREATE TABLE `menu_category_items`
(
    `id`            bigint unsigned                                           NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `menu_id`       char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单ID',
    `category_id`   char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目ID',
    `item_id`       char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品ID',
    `display_order` int             DEFAULT '0' COMMENT '显示顺序',
    `created_at`    bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at`    bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `menu_id` (`menu_id`),
    KEY `category_id` (`category_id`),
    KEY `item_id` (`item_id`)
) ENGINE = InnoDB COMMENT ='菜单-类目-菜品关联表';

CREATE TABLE floor_plans
(
    floor_plan_id   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '平面图id',
    store_id        VARCHAR(255) COMMENT '所属门店id',
    floor_plan_name BIGINT UNSIGNED COMMENT '平面图名称',
    width           INT UNSIGNED COMMENT '宽度',
    height          INT UNSIGNED COMMENT '高度',
    tables_number   INT UNSIGNED COMMENT '桌子数量',
    capacity        INT UNSIGNED COMMENT '容量',
    KEY `store_id` (`store_id`)
) COMMENT ='楼层平面图表';
CREATE TABLE `dining_tables`
(
    `merchant_id`       char(36)        NOT NULL COMMENT '商家ID',
    `dining_table_id`   bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '餐桌id',
    `store_id`          varchar(36)     DEFAULT NULL COMMENT '所属门店id',
    `floor_plan_id`     bigint unsigned DEFAULT NULL COMMENT '所属楼层平面id',
    `dining_table_name` varchar(255)    DEFAULT NULL COMMENT '餐桌名称',
    `capacity`          int unsigned    DEFAULT NULL COMMENT '容量',
    `used_capacity`     int unsigned    DEFAULT NULL COMMENT '使用容量',
    `shape`             varchar(50)     DEFAULT NULL COMMENT '形状',
    `width`             int unsigned    DEFAULT NULL COMMENT '宽度',
    `height`            int unsigned    DEFAULT NULL COMMENT '高度',
    `position_x`        int unsigned    DEFAULT NULL COMMENT '坐标x',
    `position_y`        int unsigned    DEFAULT NULL COMMENT '坐标y',
    `status`            varchar(50)     DEFAULT NULL COMMENT '状态',
    `opener`            char(36)        DEFAULT NULL COMMENT '开台服务员ID',
    PRIMARY KEY (`dining_table_id`),
    KEY `store_id` (`store_id`),
    KEY `floor_plan_id` (`floor_plan_id`)
) COMMENT ='桌子表';
-- 10/20byJames
-- 打印机档口表
CREATE TABLE `printer_door_ports`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `merchant_id`    char(36)        NOT NULL COMMENT '商家ID',
    `device_id`      char(36)        NOT NULL COMMENT '打印机ID',
    `door_port_name` varchar(255)    NOT NULL COMMENT '档口名称',
    `created_at`     bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at`     bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `device_id` (`device_id`)
) ENGINE = InnoDB COMMENT ='打印机档口表';

-- 档口打印机关联表
CREATE TABLE `door_port_printers`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `merchant_id`  char(36)        NOT NULL COMMENT '商家ID',
    `door_port_id` bigint unsigned NOT NULL COMMENT '档口ID',
    `device_id`    char(36)        NOT NULL COMMENT '打印机ID',
    `created_at`   bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at`   bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`),
    KEY `door_port_id` (`door_port_id`),
    KEY `device_id` (`device_id`)
) ENGINE = InnoDB COMMENT ='档口打印机关联表';

-- 更新item加一个档口关联字段
ALTER TABLE `items`
    ADD COLUMN `door_port_id` bigint unsigned DEFAULT NULL COMMENT '档口ID' AFTER `item_id`;

-- 打印模板表
CREATE TABLE `print_templates`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `merchant_id`      char(36)        NOT NULL COMMENT '商家ID',
    `template_name`    varchar(255)    NOT NULL COMMENT '模板名称',
    `template_content` text COMMENT '模板内容',
    `created_at`       bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at`       bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`)
) ENGINE = InnoDB COMMENT ='打印模板表';

-- 订单表
CREATE TABLE `orders`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `merchant_id`  char(36)        NOT NULL COMMENT '商家ID',
    `order_no`     varchar(255)    NOT NULL COMMENT '订单号',
    `order_type`   varchar(50)     NOT NULL COMMENT '订单类型,堂食/外带/扫码订单',
    `order_status` varchar(50)     NOT NULL COMMENT '订单状态',
    `total_amount` decimal(10, 2)  NOT NULL COMMENT '总金额',
    `created_at`   bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at`   bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`)
) ENGINE = InnoDB COMMENT ='订单表';

-- 商家银行卡表
CREATE TABLE `merchant_bank_cards`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `merchant_id`  char(36)        NOT NULL COMMENT '商家ID',
    `bank_card_no` varchar(255)    NOT NULL COMMENT '银行卡号',
    `bank_name`    varchar(255)    NOT NULL COMMENT '银行名称',
    `created_at`   bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `updated_at`   bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `merchant_id` (`merchant_id`)
) ENGINE = InnoDB COMMENT ='商家银行卡表';
