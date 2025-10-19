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