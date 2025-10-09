-- ==============================
-- V1__create_pos_system.sql
-- POS系统完整数据库设计 - Monty风格
-- 包含所有表结构、初始数据、优化和性能调优
-- ==============================

-- 使用现有数据库
USE pos_db;

-- 设置默认存储引擎和字符集
SET default_storage_engine = InnoDB;
SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- =================================
-- 1. 商家与门店管理模块（Square风格）
-- =================================

-- 1.1 商家表 (merchants) - Square风格商家注册，使用UUID主键
CREATE TABLE merchants (
    id CHAR(36) NOT NULL PRIMARY KEY COMMENT '商家ID（UUID格式，如MRC-xxx）',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '商家邮箱',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    business_name VARCHAR(255) NOT NULL COMMENT '企业名称',
    industry VARCHAR(100) NOT NULL COMMENT '行业类型',
    currency CHAR(3) NOT NULL DEFAULT 'USD' COMMENT '币种',
    country CHAR(2) NOT NULL DEFAULT 'US' COMMENT '国家代码',
    status VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '商家状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE,

    INDEX idx_merchants_email (email),
    INDEX idx_merchants_status (status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家表（Square风格，UUID主键）';

-- 1.2 商家银行账户表 (merchant_bank_accounts) - 独立管理银行账户
CREATE TABLE merchant_bank_accounts (
    id CHAR(36) NOT NULL PRIMARY KEY COMMENT '银行账户ID（UUID格式）',
    merchant_id CHAR(36) NOT NULL COMMENT '所属商家ID',
    account_number VARCHAR(64) NOT NULL COMMENT '银行账户号',
    routing_number VARCHAR(64) NOT NULL COMMENT '银行路由号',
    account_holder VARCHAR(255) NOT NULL COMMENT '银行账户持有人',
    account_type VARCHAR(50) DEFAULT 'CHECKING' COMMENT '账户类型（CHECKING/SAVINGS）',
    bank_name VARCHAR(255) COMMENT '银行名称',
    is_primary BOOLEAN DEFAULT FALSE COMMENT '是否为主账户',
    is_verified BOOLEAN DEFAULT FALSE COMMENT '是否已验证',
    status VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '账户状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE,

    INDEX idx_bank_accounts_merchant (merchant_id, is_deleted),
    INDEX idx_bank_accounts_primary (merchant_id, is_primary, is_deleted),
    INDEX idx_bank_accounts_status (status, is_verified, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家银行账户表';

-- 1.3 门店表 (stores) - 使用UUID主键
CREATE TABLE stores (
    id CHAR(36) NOT NULL PRIMARY KEY COMMENT '门店ID（UUID格式，如LOC-xxx）',
    merchant_id CHAR(36) NOT NULL COMMENT '所属商家ID',
    store_name VARCHAR(255) NOT NULL COMMENT '门店名称',
    address VARCHAR(255) COMMENT '门店地址',
    timezone VARCHAR(64) DEFAULT 'UTC' COMMENT '时区',
    status VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '门店状态',
    tax_rate DECIMAL(5,4) DEFAULT 0.0000 COMMENT '默认税率',
    currency VARCHAR(3) DEFAULT 'USD' COMMENT '币种',
    business_hours JSON COMMENT '营业时间配置',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE,

    INDEX idx_stores_merchant (merchant_id, is_deleted),
    INDEX idx_stores_status (status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='门店表（UUID主键）';

-- 1.3 角色表 (roles) - 权限角色 [MOVED BEFORE employees]
CREATE TABLE roles (
    role_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '角色主键（UUID）',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色代码',
    description TEXT COMMENT '角色描述',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_roles_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- 1.4 权限表 (permissions) - 细粒度权限 [MOVED BEFORE employees]
CREATE TABLE permissions (
    permission_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '权限主键（UUID）',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限代码',
    resource VARCHAR(50) NOT NULL COMMENT '资源标识',
    action VARCHAR(50) NOT NULL COMMENT '操作标识',
    description TEXT COMMENT '权限描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_permissions_code (permission_code),
    UNIQUE KEY uk_permissions_resource_action (resource, action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

-- 1.5 角色权限关联表 (role_permissions) [MOVED BEFORE employees]
CREATE TABLE role_permissions (
    role_id CHAR(36) NOT NULL COMMENT '角色ID',
    permission_id CHAR(36) NOT NULL COMMENT '权限ID',
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',

    PRIMARY KEY (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';

-- 1.6 用户表 (employees) - 门店员工 [MOVED AFTER ROLES]
CREATE TABLE `employees`
(
    `employees_id`  char(36)     NOT NULL COMMENT '用户ID Square风格',
    `merchant_id`   char(36)     NOT NULL COMMENT '商家ID',
    `store_id`      char(36)     NOT NULL COMMENT '门店ID',
    `email`         varchar(100) NOT NULL COMMENT '邮箱（唯一）',
    `pass_code`     varchar(255)          DEFAULT NULL COMMENT 'PIN码哈希',
    `first_name`    varchar(50)           DEFAULT NULL COMMENT '名',
    `last_name`     varchar(50)           DEFAULT NULL COMMENT '姓',
    `role_id`       char(36)     NOT NULL COMMENT '角色ID',
    `status`        varchar(50)  NOT NULL DEFAULT 'ACTIVE' COMMENT '账号状态',
    `last_login_at` bigint unsigned DEFAULT NULL COMMENT '最后登录时间',
    `created_at`    bigint unsigned DEFAULT NULL,
    `updated_at`    bigint unsigned DEFAULT NULL,
    `is_deleted`    tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记',
    PRIMARY KEY (`employees_id`) USING BTREE,
    KEY             `merchant_id` (`merchant_id`),
    KEY             `store_id` (`store_id`),
    KEY             `email` (`email`),
    KEY             `first_name` (`first_name`),
    KEY             `last_name` (`last_name`),
    KEY             `role_id` (`role_id`),
    KEY             `status` (`status`),
    KEY             `is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';

-- =================================
-- 2. 商品与库存管理模块
-- =================================

-- 2.1 商品分类表 (categories)
CREATE TABLE categories (
    category_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '分类主键（UUID）',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    display_order INT DEFAULT 0 COMMENT '显示顺序',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_categories_store (store_id, is_active, is_deleted),
    INDEX idx_categories_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';

-- 2.2 商品表 (products)
CREATE TABLE products (
    product_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '商品ID（UUID）',
    merchant_id CHAR(36) NOT NULL COMMENT '商家ID',
    store_id CHAR(36) NOT NULL,
    category_id CHAR(36) COMMENT '商品分类',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '销售价格',
    image_url VARCHAR(500) COMMENT '商品图片URL',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否上架',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36) DEFAULT NULL COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by CHAR(36) DEFAULT NULL COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE,

    INDEX idx_products_store (merchant_id, store_id),
    INDEX idx_products_pos_list (store_id, category_id, is_active, is_deleted, product_name, price),
    INDEX idx_products_sync (store_id, updated_at, is_deleted),
    FULLTEXT INDEX idx_products_search (product_name, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

-- 2.3 库存表 (inventory)
CREATE TABLE inventory (
    inventory_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '库存主键（UUID）',
    product_id CHAR(36) NOT NULL COMMENT '商品ID',
    current_stock INT NOT NULL DEFAULT 0 COMMENT '当前库存',
    min_stock INT DEFAULT 0 COMMENT '最低库存阈值',
    max_stock INT DEFAULT 0 COMMENT '最高库存阈值',
    cost_price DECIMAL(10,2) COMMENT '成本价格',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_inventory_product (product_id),
    INDEX idx_inventory_stock_level (current_stock, min_stock),
    INDEX idx_inventory_alert (current_stock, min_stock, product_id),
    INDEX idx_inventory_cost (product_id, cost_price, current_stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存表';

-- =================================
--  菜单与菜单管理模块
-- =================================

--  菜单分类表 (menu_categories)
CREATE TABLE menu_categories (
     id CHAR(36) NOT NULL PRIMARY KEY COMMENT '分类ID（UUID）',
     org_id CHAR(36) NOT NULL COMMENT '组织ID（UUID）',
     store_id CHAR(36) NOT NULL COMMENT '门店ID（UUID）',
     name VARCHAR(255) NOT NULL COMMENT '分类名称',
     sort_order INT DEFAULT 0 COMMENT '排序序号',
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     created_by CHAR(36) COMMENT '创建人UUID',
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     updated_by CHAR(36) COMMENT '更新人UUID',
     is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除标记',

     INDEX idx_org_store (org_id, store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单分类表';


--  菜单项表 (menu_items)
CREATE TABLE menu_items (
    id CHAR(36) NOT NULL PRIMARY KEY COMMENT '菜品ID（UUID）',
    org_id CHAR(36) NOT NULL COMMENT '组织ID（UUID）',
    store_id CHAR(36) NOT NULL COMMENT '门店ID（UUID）',
    category_id CHAR(36) NOT NULL COMMENT '分类ID（UUID）',
    name VARCHAR(255) NOT NULL COMMENT '菜品名称',
    description TEXT COMMENT '菜品描述',
    price DECIMAL(12,2) NOT NULL COMMENT '价格',
    currency CHAR(3) DEFAULT 'USD' COMMENT '货币代码',
    image_url VARCHAR(500) COMMENT '图片URL',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除标记',

    INDEX idx_org_store_cat (org_id, store_id, category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单项表';


-- =================================
-- 3. 订单与支付管理模块
-- =================================

-- 3.1 客户表 (customers)
CREATE TABLE customers (
    customer_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '客户主键（UUID）',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱地址',
    points_balance INT DEFAULT 0 COMMENT '积分余额',
    membership_level VARCHAR(20) DEFAULT 'REGULAR' COMMENT '会员等级',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_customers_store (store_id, is_deleted),
    INDEX idx_customers_phone (phone),
    INDEX idx_customers_email (email),
    INDEX idx_customers_contact (store_id, phone, email, is_deleted),
    INDEX idx_customers_membership (store_id, membership_level, points_balance, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户表';


-- 3.2 订单表 (orders)
CREATE TABLE orders (
    order_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '订单ID（UUID）',
    merchant_id CHAR(36) NOT NULL COMMENT '商家ID',
    store_id CHAR(36) NOT NULL,
    employees_id CHAR(36) NOT NULL COMMENT '下单用户',
    customer_id CHAR(36) COMMENT '客户ID',
    order_number VARCHAR(50) COMMENT '订单编号',
    idempotency_key VARCHAR(100) COMMENT '幂等性键',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '订单总金额',
    tax_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '税费金额',
    tip_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '小费金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '折扣金额',
    status ENUM('CREATED', 'CONFIRMED', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'CREATED' COMMENT '订单状态',
    payment_status ENUM('PENDING', 'PAID', 'PARTIAL', 'REFUNDED', 'FAILED') DEFAULT 'PENDING' COMMENT '支付状态',
    order_type ENUM('DINE_IN', 'TAKEOUT', 'DELIVERY') DEFAULT 'DINE_IN' COMMENT '订单类型',
    completed_at TIMESTAMP COMMENT '完成时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36) DEFAULT NULL COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by CHAR(36) DEFAULT NULL COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE,

    UNIQUE KEY uk_orders_number (order_number),
    UNIQUE KEY uk_orders_idempotency (idempotency_key),
    INDEX idx_orders_store (merchant_id, store_id),
    INDEX idx_orders_user (employees_id),
    INDEX idx_orders_dashboard (store_id, created_at, status, is_deleted),
    INDEX idx_orders_status_processing (status, payment_status, store_id, updated_at),
    INDEX idx_orders_user_history (employees_id, created_at, status),
    INDEX idx_orders_customer_history (customer_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

-- 3.3 订单明细表 (order_items)
CREATE TABLE order_items (
    order_item_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '订单项ID（UUID）',
    merchant_id CHAR(36) NOT NULL COMMENT '商家ID',
    store_id CHAR(36) NOT NULL,
    order_id CHAR(36) NOT NULL COMMENT '订单ID',
    product_id CHAR(36) NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
    subtotal DECIMAL(12,2) NOT NULL COMMENT '小计金额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by CHAR(36) DEFAULT NULL COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by CHAR(36) DEFAULT NULL COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE,

    INDEX idx_order_items_order (order_id),
    INDEX idx_order_items_product (product_id),
    INDEX idx_order_items_detail (order_id, product_id, quantity, unit_price, subtotal, is_deleted),
    INDEX idx_order_items_product_stats (product_id, created_at, quantity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';

-- 3.4 支付表 (payments)
CREATE TABLE payments (
    payment_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '支付主键（UUID）',
    order_id CHAR(36) NOT NULL COMMENT '订单ID',
    idempotency_key VARCHAR(100) NOT NULL COMMENT '幂等性键',
    payment_method VARCHAR(50) NOT NULL COMMENT '支付方式',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    transaction_id VARCHAR(100) COMMENT '第三方交易ID',
    status ENUM('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING' COMMENT '支付状态',
    processed_at TIMESTAMP COMMENT '处理时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_payments_idempotency (idempotency_key),
    INDEX idx_payments_order (order_id),
    INDEX idx_payments_status (status, processed_at),
    INDEX idx_payments_transaction (transaction_id),
    INDEX idx_payments_status_time (status, processed_at, order_id),
    INDEX idx_payments_transaction_lookup (transaction_id, status, amount),
    INDEX idx_payments_refund (order_id, status, amount)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付表';

-- 3.5 优惠券表 (coupons)
CREATE TABLE coupons (
    coupon_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '优惠券主键（UUID）',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    coupon_code VARCHAR(50) NOT NULL COMMENT '优惠券代码',
    discount_type ENUM('FIXED_AMOUNT', 'PERCENTAGE') NOT NULL COMMENT '折扣类型',
    discount_value DECIMAL(10,2) NOT NULL COMMENT '折扣值',
    min_order_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低订单金额',
    valid_from TIMESTAMP NOT NULL COMMENT '生效时间',
    valid_until TIMESTAMP NOT NULL COMMENT '失效时间',
    usage_limit INT DEFAULT 0 COMMENT '使用次数限制(0=无限制)',
    used_count INT DEFAULT 0 COMMENT '已使用次数',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_coupons_code (coupon_code),
    INDEX idx_coupons_store (store_id, is_active, is_deleted),
    INDEX idx_coupons_validity (valid_from, valid_until)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券表';

-- 3.6 订单优惠券关联表 (order_coupons)
CREATE TABLE order_coupons (
    order_id CHAR(36) NOT NULL COMMENT '订单ID',
    coupon_id CHAR(36) NOT NULL COMMENT '优惠券ID',
    discount_applied DECIMAL(10,2) NOT NULL COMMENT '实际折扣金额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '使用时间',
    created_by CHAR(36) COMMENT '操作人UUID',

    PRIMARY KEY (order_id, coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单优惠券关联表';

-- =================================
-- 4. 考勤与会话管理模块
-- =================================

-- 4.1 考勤表 (attendance)
CREATE TABLE attendance (
    attendance_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '考勤主键（UUID）',
    employees_id CHAR(36) NOT NULL COMMENT '员工ID',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    clock_in_time TIMESTAMP NOT NULL COMMENT '上班打卡时间',
    clock_out_time TIMESTAMP COMMENT '下班打卡时间',
    total_hours DECIMAL(5,2) COMMENT '工作总时长',
    idempotency_key VARCHAR(100) NOT NULL COMMENT '幂等性键',
    sync_status ENUM('SYNCED', 'PENDING', 'FAILED') DEFAULT 'SYNCED' COMMENT '同步状态',
    clock_in_month INT GENERATED ALWAYS AS (YEAR(clock_in_time) * 100 + MONTH(clock_in_time)) STORED COMMENT '打卡年月(YYYYMM)',
    clock_in_date DATE GENERATED ALWAYS AS (DATE(clock_in_time)) STORED COMMENT '打卡日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_attendance_idempotency (idempotency_key),
    INDEX idx_attendance_user_date (employees_id, clock_in_time),
    INDEX idx_attendance_store_date (store_id, clock_in_time),
    INDEX idx_attendance_sync (sync_status),
    INDEX idx_attendance_monthly (employees_id, clock_in_month, total_hours),
    INDEX idx_attendance_store_daily (store_id, clock_in_date, clock_in_time),
    INDEX idx_attendance_incomplete (employees_id, clock_in_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤表';

-- 4.2 用户会话表 (user_sessions)
CREATE TABLE user_sessions (
    session_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '会话主键（UUID）',
    employees_id CHAR(36) NOT NULL COMMENT '用户ID',
    device_id CHAR(36) COMMENT '设备ID',
    access_token VARCHAR(500) COMMENT '访问令牌',
    refresh_token VARCHAR(500) COMMENT '刷新令牌',
    access_token_expires_at TIMESTAMP COMMENT '访问令牌过期时间',
    refresh_token_expires_at TIMESTAMP COMMENT '刷新令牌过期时间',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    status VARCHAR(50) DEFAULT 'ACTIVE' COMMENT '会话状态',
    last_activity_at TIMESTAMP COMMENT '最后活动时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_sessions_user (employees_id, status, is_deleted),
    INDEX idx_sessions_access_token (access_token),
    INDEX idx_sessions_expires (access_token_expires_at, refresh_token_expires_at),
    INDEX idx_sessions_activity (last_activity_at, status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户会话表';

-- 4.3 交班记录表 (closings)
CREATE TABLE closings (
    closing_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '交班主键（UUID）',
    employees_id CHAR(36) NOT NULL COMMENT '交班员工ID',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    closing_date DATE NOT NULL COMMENT '交班日期',
    cash_counted DECIMAL(10,2) NOT NULL COMMENT '实际现金金额',
    cash_expected DECIMAL(10,2) NOT NULL COMMENT '预期现金金额',
    difference DECIMAL(10,2) NOT NULL COMMENT '差额',
    sync_status ENUM('SYNCED', 'PENDING', 'FAILED') DEFAULT 'SYNCED' COMMENT '同步状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_closings_store_date (store_id, closing_date),
    INDEX idx_closings_user (employees_id),
    INDEX idx_closings_sync (sync_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交班记录表';

-- 4.4 收据记录表 (receipts)
CREATE TABLE receipts (
    receipt_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '收据主键（UUID）',
    order_id CHAR(36) NOT NULL COMMENT '订单ID',
    delivery_method ENUM('PRINT', 'SMS', 'EMAIL') NOT NULL COMMENT '发送方式',
    recipient VARCHAR(200) COMMENT '接收方',
    sent_at TIMESTAMP COMMENT '发送时间',
    status ENUM('PENDING', 'SENT', 'FAILED') DEFAULT 'PENDING' COMMENT '发送状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_receipts_order (order_id),
    INDEX idx_receipts_status (status, sent_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收据记录表';

-- =================================
-- 5. 系统管理与配置模块
-- =================================

-- 5.1 设备表 (devices)
CREATE TABLE `devices` (
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
    `is_deleted`    tinyint(1) DEFAULT '0' COMMENT '软删除标识',
    PRIMARY KEY (`device_id`),
    KEY             `is_deleted` (`is_deleted`) USING BTREE,
    KEY             `ip_address` (`ip_address`),
    KEY             `mac_address` (`mac_address`),
    KEY             `last_login_at` (`last_login_at`),
    KEY             `registered_at` (`registered_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备表';


-- 5.2 设备码表 (device_codes) - Square风格激活码
CREATE TABLE device_codes (
    device_code_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '设备码主键（UUID）',
    device_code VARCHAR(12) NOT NULL UNIQUE COMMENT '设备激活码，12位数字字母混合',
    device_id CHAR(36) COMMENT '关联的设备ID（可选）',
    device_fingerprint VARCHAR(255) COMMENT '设备指纹信息',
    status VARCHAR(20) NOT NULL DEFAULT 'UNUSED' COMMENT '设备码状态：UNUSED/BOUND/EXPIRED',
    activation_attempts INT DEFAULT 0 COMMENT '激活尝试次数',
    max_attempts INT DEFAULT 3 COMMENT '最大激活尝试次数',
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '发行时间',
    expired_at TIMESTAMP COMMENT '过期时间',
    bound_at TIMESTAMP COMMENT '绑定时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_device_codes_code (device_code),
    INDEX idx_device_codes_device (device_id, status),
    INDEX idx_device_codes_status (status, expired_at),
    INDEX idx_device_codes_fingerprint (device_fingerprint),
    INDEX idx_device_codes_attempts (activation_attempts, status),
    INDEX idx_device_codes_issued (issued_at, status),
    UNIQUE KEY uk_device_codes_code (device_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备激活码表，Square风格即时激活';


-- 5.3 税务规则表 (tax_rules)
CREATE TABLE tax_rules (
    tax_rule_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '税务规则主键（UUID）',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    tax_name VARCHAR(100) NOT NULL COMMENT '税种名称',
    tax_rate DECIMAL(5,4) NOT NULL COMMENT '税率',
    tax_type ENUM('STATE_TAX', 'CITY_TAX', 'FEDERAL_TAX', 'VAT') NOT NULL COMMENT '税种类型',
    applicable_to VARCHAR(100) COMMENT '适用范围',
    effective_from DATE NOT NULL COMMENT '生效日期',
    effective_until DATE COMMENT '失效日期',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_tax_rules_store (store_id, is_active, is_deleted),
    INDEX idx_tax_rules_effective (effective_from, effective_until)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='税务规则表';

-- 5.3 通知表 (notifications)
CREATE TABLE notifications (
    notification_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '通知主键（UUID）',
    store_id CHAR(36) COMMENT '所属店铺',
    employees_id CHAR(36) COMMENT '目标用户',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    message TEXT NOT NULL COMMENT '通知内容',
    type VARCHAR(50) NOT NULL COMMENT '通知类型',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    read_at TIMESTAMP COMMENT '阅读时间',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    INDEX idx_notifications_user (employees_id, is_read, created_at),
    INDEX idx_notifications_store (store_id, type, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知表';

-- =================================
-- 6. 报表与汇总模块
-- =================================

-- 6.1 日销售汇总表 (daily_sales_reports)
CREATE TABLE daily_sales_reports (
    report_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '报表主键（UUID）',
    store_id CHAR(36) NOT NULL COMMENT '所属店铺',
    report_date DATE NOT NULL COMMENT '报表日期',
    total_sales_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '总销售额',
    total_orders INT NOT NULL DEFAULT 0 COMMENT '订单总数',
    average_order_value DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '客单价',
    total_tips DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '小费总额',
    top_product_id CHAR(36) COMMENT '热门商品ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by CHAR(36) COMMENT '创建人UUID',
    updated_by CHAR(36) COMMENT '更新人UUID',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '软删除标识',

    UNIQUE KEY uk_daily_reports_store_date (store_id, report_date),
    INDEX idx_daily_reports_date (report_date),
    INDEX idx_daily_reports_store (store_id, report_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日销售汇总表';

-- =================================
-- 7. 性能监控和备份表
-- =================================

-- 7.1 性能监控表
CREATE TABLE performance_metrics (
    metric_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '指标ID（UUID）',
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15,4) NOT NULL,
    store_id CHAR(36),
    measured_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_performance_metrics_name_time (metric_name, measured_at),
    INDEX idx_performance_metrics_store (store_id, measured_at)
) ENGINE=InnoDB COMMENT='性能指标监控表';

-- 7.2 备份历史记录表
CREATE TABLE backup_history (
    backup_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '备份ID（UUID）',
    backup_type ENUM('FULL', 'INCREMENTAL', 'DIFFERENTIAL') NOT NULL,
    backup_path VARCHAR(500) NOT NULL,
    backup_size BIGINT COMMENT '备份文件大小(字节)',
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    status ENUM('RUNNING', 'COMPLETED', 'FAILED') DEFAULT 'RUNNING',
    error_message TEXT,
    created_by BIGINT,

    INDEX idx_backup_history_time (start_time DESC),
    INDEX idx_backup_history_type (backup_type, status)
) ENGINE=InnoDB COMMENT='备份历史记录表';

-- 7.3 系统告警表
CREATE TABLE system_alerts (
    alert_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '警告ID（UUID）',
    alert_type VARCHAR(50) NOT NULL,
    alert_level ENUM('INFO', 'WARNING', 'ERROR', 'CRITICAL') NOT NULL,
    alert_message TEXT NOT NULL,
    alert_data JSON,
    resolved BOOLEAN DEFAULT FALSE,
    resolved_at TIMESTAMP NULL,
    resolved_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_system_alerts_type_level (alert_type, alert_level, created_at),
    INDEX idx_system_alerts_resolved (resolved, created_at)
) ENGINE=InnoDB COMMENT='系统告警记录表';


-- 示例商家数据（Square风格，UUID主键）
INSERT INTO merchants (id, email, password_hash, business_name, industry, currency, country, status, created_by) VALUES
('MRC-174000000001', 'owner@trip7cafe.com', '$2a$10$rMgr4YOhVNcXP7Qhp8jQHe7vKbQZ8tXkF2VyLjKrX3nP9sWqR1tGu', 'Trip7 Cafe Holdings', 'restaurant', 'USD', 'US', 'ACTIVE', 'MRC-174000000001');

-- 示例银行账户数据
INSERT INTO merchant_bank_accounts (id, merchant_id, account_number, routing_number, account_holder, account_type, bank_name, is_primary, is_verified, status, created_by) VALUES
('BA-174000000001', 'MRC-174000000001', '1234567890', '987654321', 'Trip7 Cafe Holdings', 'CHECKING', 'Bank of America', TRUE, TRUE, 'ACTIVE', 'MRC-174000000001');

-- 示例门店数据
INSERT INTO stores (id, merchant_id, store_name, address, timezone, status, tax_rate, currency, business_hours, created_by) VALUES
('LOC-174000000001', 'MRC-174000000001', '新宿店', '東京都新宿区1-2-3', 'Asia/Tokyo', 'ACTIVE', 0.0875, 'USD',
'{"monday": {"open": "07:00", "close": "22:00"}, "tuesday": {"open": "07:00", "close": "22:00"}, "wednesday": {"open": "07:00", "close": "22:00"}, "thursday": {"open": "07:00", "close": "22:00"}, "friday": {"open": "07:00", "close": "23:00"}, "saturday": {"open": "08:00", "close": "23:00"}, "sunday": {"open": "08:00", "close": "21:00"}}', 'MRC-174000000001');

-- 示例管理员用户
INSERT INTO `employees` (`employees_id`, `merchant_id`, `store_id`, `email`, `pass_code`, `first_name`, `last_name`, `role_id`, `status`, `last_login_at`, `created_at`, `updated_at`, `is_deleted`) VALUES ('USR-174000000001', 'MRC-174000000001', 'LOC-174000000001', 'admin@trip7cafe.com', NULL, 'Admin', 'User', 'ROL-00004', 'ACTIVE', NULL, 1760017863000, 1760017863000, 0);

-- 示例员工用户（使用正确的merchant_id和store_id）
INSERT INTO `employees` (`employees_id`, `merchant_id`, `store_id`, `email`, `pass_code`, `first_name`, `last_name`, `role_id`, `status`, `last_login_at`, `created_at`, `updated_at`, `is_deleted`) VALUES ('USR-174000000002', 'MRC-174000000001', 'LOC-174000000001', 'cashier.john@trip7cafe.com', NULL, 'John', 'Doe', 'ROL-00001', 'ACTIVE', 1760017863000, 1760017863000, 1760017863000, 0);
INSERT INTO `employees` (`employees_id`, `merchant_id`, `store_id`, `email`, `pass_code`, `first_name`, `last_name`, `role_id`, `status`, `last_login_at`, `created_at`, `updated_at`, `is_deleted`) VALUES ('USR-174000000003', 'MRC-174000000001', 'LOC-174000000001', 'alice.server@trip7cafe.com', NULL, 'Alice', 'Smith', 'ROL-00002', 'ACTIVE', 1760017863000, 1760017863000, 1760017863000, 0);
INSERT INTO `employees` (`employees_id`, `merchant_id`, `store_id`, `email`, `pass_code`, `first_name`, `last_name`, `role_id`, `status`, `last_login_at`, `created_at`, `updated_at`, `is_deleted`) VALUES ('USR-174000000004', 'MRC-174000000001', 'LOC-174000000001', 'bob.manager@trip7cafe.com', NULL, 'Bob', 'Lee', 'ROL-00003', 'ACTIVE', 1760017863000, 1760017863000, 1760017863000, 0);

-- 示例商品分类和商品
INSERT INTO categories (category_id, store_id, category_name, description, display_order, created_by) VALUES
('CAT-174000000001', 'LOC-174000000001', '咖啡', '各类咖啡饮品', 1, 'MRC-174000000001'),
('CAT-174000000002', 'LOC-174000000001', '茶饮', '茶类饮品', 2, 'MRC-174000000001'),
('CAT-174000000003', 'LOC-174000000001', '甜点', '蛋糕和甜品', 3, 'MRC-174000000001'),
('CAT-174000000004', 'LOC-174000000001', '轻食', '三明治和沙拉', 4, 'MRC-174000000001');

-- 插入商品数据
INSERT INTO products (product_id, merchant_id, store_id, category_id, product_name, description, price, is_active, created_by) VALUES
-- 咖啡类
('PRD-174000000001', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000001', '美式咖啡', '经典美式黑咖啡', 3.50, TRUE, 'MRC-174000000001'),
('PRD-174000000002', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000001', '拿铁', '香浓牛奶咖啡', 4.50, TRUE, 'MRC-174000000001'),
('PRD-174000000003', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000001', '卡布奇诺', '意式卡布奇诺', 4.00, TRUE, 'MRC-174000000001'),
('PRD-174000000004', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000001', '摩卡', '巧克力摩卡咖啡', 5.00, TRUE, 'MRC-174000000001'),
-- 茶饮类
('PRD-174000000005', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000002', '绿茶', '清香绿茶', 2.50, TRUE, 'MRC-174000000001'),
('PRD-174000000006', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000002', '红茶', '经典红茶', 2.50, TRUE, 'MRC-174000000001'),
('PRD-174000000007', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000002', '柠檬茶', '清爽柠檬茶', 3.00, TRUE, 'MRC-174000000001'),
-- 甜点类
('PRD-174000000008', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000003', '芝士蛋糕', '纽约风味芝士蛋糕', 6.50, TRUE, 'MRC-174000000001'),
('PRD-174000000009', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000003', '巧克力蛋糕', '浓郁巧克力蛋糕', 5.50, TRUE, 'MRC-174000000001'),
('PRD-174000000010', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000003', '提拉米苏', '意式提拉米苏', 7.00, TRUE, 'MRC-174000000001'),
-- 轻食类
('PRD-174000000011', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000004', '火腿三明治', '经典火腿芝士三明治', 8.50, TRUE, 'MRC-174000000001'),
('PRD-174000000012', 'MRC-174000000001', 'LOC-174000000001', 'CAT-174000000004', '凯撒沙拉', '新鲜凯撒沙拉', 9.00, TRUE, 'MRC-174000000001');

-- 初始化库存数据
INSERT INTO inventory (inventory_id, product_id, current_stock, min_stock, max_stock, cost_price, created_by) VALUES
('INV-174000000001', 'PRD-174000000001', 100, 10, 500, 2.10, 'MRC-174000000001'),
('INV-174000000002', 'PRD-174000000002', 100, 10, 500, 2.70, 'MRC-174000000001'),
('INV-174000000003', 'PRD-174000000003', 100, 10, 500, 2.40, 'MRC-174000000001'),
('INV-174000000004', 'PRD-174000000004', 100, 10, 500, 3.00, 'MRC-174000000001'),
('INV-174000000005', 'PRD-174000000005', 100, 10, 500, 1.50, 'MRC-174000000001'),
('INV-174000000006', 'PRD-174000000006', 100, 10, 500, 1.50, 'MRC-174000000001'),
('INV-174000000007', 'PRD-174000000007', 100, 10, 500, 1.80, 'MRC-174000000001'),
('INV-174000000008', 'PRD-174000000008', 100, 10, 500, 3.90, 'MRC-174000000001'),
('INV-174000000009', 'PRD-174000000009', 100, 10, 500, 3.30, 'MRC-174000000001'),
('INV-174000000010', 'PRD-174000000010', 100, 10, 500, 4.20, 'MRC-174000000001'),
('INV-174000000011', 'PRD-174000000011', 100, 10, 500, 5.10, 'MRC-174000000001'),
('INV-174000000012', 'PRD-174000000012', 100, 10, 500, 5.40, 'MRC-174000000001');

-- 完成POS系统数据库初始化

