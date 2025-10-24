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
    `menu_id`     BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单id',
    `merchant_id` char(36) COMMENT '商户id',
    `store_id`    char(36) COMMENT '所属门店id',
    `menu_name`   VARCHAR(255) COMMENT '菜单名称',
    PRIMARY KEY (`menu_id`)
) COMMENT ='菜单';

CREATE TABLE `menu_categories`
(
    `category_id`   BIGINT NOT NULL AUTO_INCREMENT COMMENT '类别id',
    `menu_id`       BIGINT NOT NULL COMMENT '所属菜单id',
    `merchant_id`   CHAR(36) COMMENT '商户id',
    `store_id`      CHAR(36) COMMENT '所属门店id',
    `category_name` VARCHAR(255) COMMENT '类别名称',
    PRIMARY KEY (`category_id`),
    KEY `menu_id` (`menu_id`),
    KEY `merchant_id` (`merchant_id`),
    KEY `store_id` (`store_id`)
) COMMENT ='菜单类别';

-- 菜品菜单项目表
CREATE TABLE `menu_items`
(
    `item_id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '单品id',
    `merchant_id`      varchar(255)             DEFAULT NULL COMMENT '商户id',
    `store_id`         varchar(255)             DEFAULT NULL COMMENT '所属门店id',
    `item_name`        varchar(255)             DEFAULT NULL COMMENT '单品名称',
    `item_description` text COMMENT '菜品简介',
    `item_price`       decimal(10, 2)           DEFAULT NULL COMMENT '单价',
    `note_tags`        json                     DEFAULT NULL COMMENT '注释标记',
    `item_type`        varchar(255)    NOT NULL DEFAULT 'Item' COMMENT '菜单项类型',
    `item_image`       text COMMENT '图片URL',
    PRIMARY KEY (`item_id`),
    KEY `idx_merchant_id` (`merchant_id`) COMMENT '商户ID索引',
    KEY `idx_store_id` (`store_id`) COMMENT '门店ID索引'
) COMMENT ='菜品菜单项目表';

-- 菜单类别与单品关联表
CREATE TABLE `menu_category_items`
(
    `cat_item_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '中间表 id',
    `merchant_id` VARCHAR(255)    DEFAULT NULL COMMENT '商家ID',
    `category_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '菜单类别id',
    `item_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '单品id',
    PRIMARY KEY (`cat_item_id`),
    INDEX `idx_merchant_id` (`merchant_id`) COMMENT '商家ID索引',
    INDEX `idx_category_id` (`category_id`) COMMENT '菜单类别索引',
    INDEX `idx_item_id` (`item_id`) COMMENT '单品索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单类别与单品关联表';

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

-- 文件表
CREATE TABLE `files`
(
    `file_id`      bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `owner_id`     varchar(255)    DEFAULT NULL COMMENT '所有者',
    `owner_type`   varchar(50)     DEFAULT NULL COMMENT '所有者类型',
    `merchant_id`  varchar(255)    DEFAULT NULL COMMENT '商户id',
    `content_type` varchar(255)    DEFAULT NULL COMMENT '文件类型',
    `file_name`    varchar(255)    DEFAULT NULL COMMENT '文件名',
    `object_name`  varchar(255)    DEFAULT NULL COMMENT 'minio对象名',
    `file_size`    bigint unsigned DEFAULT NULL COMMENT '文件大小',
    `preview_url`  varchar(500)    DEFAULT NULL COMMENT '预览地址URL',
    `create_at`    bigint unsigned DEFAULT NULL COMMENT '创建时间',
    `update_at`    bigint unsigned DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`file_id`)
) COMMENT ='文件表';
