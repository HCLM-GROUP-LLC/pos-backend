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

-- 1.3 角色表 (roles) - 权限角色 [MOVED BEFORE USERS]
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

-- 1.4 权限表 (permissions) - 细粒度权限 [MOVED BEFORE USERS]
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

-- 1.5 角色权限关联表 (role_permissions) [MOVED BEFORE USERS]
CREATE TABLE role_permissions (
    role_id CHAR(36) NOT NULL COMMENT '角色ID',
    permission_id CHAR(36) NOT NULL COMMENT '权限ID',
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '授权时间',

    PRIMARY KEY (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';

-- 1.6 用户表 (users) - 门店员工 [MOVED AFTER ROLES]
CREATE TABLE users (
    user_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '用户ID（UUID格式）',
    merchant_id CHAR(36) NOT NULL COMMENT '商家ID',
    store_id CHAR(36) NOT NULL COMMENT '门店ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
    email VARCHAR(100) NOT NULL COMMENT '邮箱（唯一）',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    pin_hash VARCHAR(255) COMMENT 'PIN码哈希',
    first_name VARCHAR(50) COMMENT '名',
    last_name VARCHAR(50) COMMENT '姓',
    role_id CHAR(36) NOT NULL COMMENT '角色ID',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT '账号状态',
    salary DECIMAL(10,2) COMMENT '薪资',
    hire_date DATE COMMENT '入职日期',
    last_login_at TIMESTAMP COMMENT '最后登录时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,

    UNIQUE INDEX idx_users_username (merchant_id, username),
    UNIQUE INDEX idx_users_email (merchant_id, email),
    INDEX idx_users_store (store_id),
    INDEX idx_users_login (email, password_hash, is_deleted),
    INDEX idx_users_store_status (store_id, status, is_deleted, hire_date),
    INDEX idx_users_role (role_id, status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表（门店员工，UUID外键）';

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
    user_id CHAR(36) NOT NULL COMMENT '下单用户',
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
    INDEX idx_orders_user (user_id),
    INDEX idx_orders_dashboard (store_id, created_at, status, is_deleted),
    INDEX idx_orders_status_processing (status, payment_status, store_id, updated_at),
    INDEX idx_orders_user_history (user_id, created_at, status),
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
    user_id CHAR(36) NOT NULL COMMENT '员工ID',
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
    INDEX idx_attendance_user_date (user_id, clock_in_time),
    INDEX idx_attendance_store_date (store_id, clock_in_time),
    INDEX idx_attendance_sync (sync_status),
    INDEX idx_attendance_monthly (user_id, clock_in_month, total_hours),
    INDEX idx_attendance_store_daily (store_id, clock_in_date, clock_in_time),
    INDEX idx_attendance_incomplete (user_id, clock_in_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤表';

-- 4.2 用户会话表 (user_sessions)
CREATE TABLE user_sessions (
    session_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '会话主键（UUID）',
    user_id CHAR(36) NOT NULL COMMENT '用户ID',
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

    INDEX idx_sessions_user (user_id, status, is_deleted),
    INDEX idx_sessions_access_token (access_token),
    INDEX idx_sessions_expires (access_token_expires_at, refresh_token_expires_at),
    INDEX idx_sessions_activity (last_activity_at, status, is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户会话表';

-- 4.3 交班记录表 (closings)
CREATE TABLE closings (
    closing_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '交班主键（UUID）',
    user_id CHAR(36) NOT NULL COMMENT '交班员工ID',
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
    INDEX idx_closings_user (user_id),
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
    user_id CHAR(36) COMMENT '目标用户',
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

    INDEX idx_notifications_user (user_id, is_read, created_at),
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

-- =================================
-- 8. JSON字段虚拟列优化
-- =================================

-- 店铺营业时间虚拟列（使用动态SQL检查是否存在）
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'stores' AND COLUMN_NAME = 'monday_open') = 0,
    'ALTER TABLE stores ADD COLUMN monday_open TIME GENERATED ALWAYS AS (STR_TO_DATE(JSON_UNQUOTE(JSON_EXTRACT(business_hours, ''$.monday.open'')), ''%H:%i'')) VIRTUAL', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'stores' AND COLUMN_NAME = 'monday_close') = 0,
    'ALTER TABLE stores ADD COLUMN monday_close TIME GENERATED ALWAYS AS (STR_TO_DATE(JSON_UNQUOTE(JSON_EXTRACT(business_hours, ''$.monday.close'')), ''%H:%i'')) VIRTUAL', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 创建索引（使用动态SQL检查是否存在）
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'stores' AND INDEX_NAME = 'idx_stores_monday_hours') = 0,
    'CREATE INDEX idx_stores_monday_hours ON stores(monday_open, monday_close)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 使用虚拟列建索引，而非表达式索引
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'stores' AND INDEX_NAME = 'idx_stores_monday_open') = 0,
    'CREATE INDEX idx_stores_monday_open ON stores(monday_open)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- =================================
-- 9. CHECK约束
-- =================================

-- 价格约束
-- 产品价格约束（如果约束已存在则跳过）
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_products_price_positive' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE products ADD CONSTRAINT chk_products_price_positive CHECK (price > 0)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 订单项价格约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_order_items_price_positive' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE order_items ADD CONSTRAINT chk_order_items_price_positive CHECK (unit_price > 0 AND subtotal >= 0)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 支付金额约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_payments_amount_positive' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE payments ADD CONSTRAINT chk_payments_amount_positive CHECK (amount > 0)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 库存约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_inventory_stock_non_negative' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE inventory ADD CONSTRAINT chk_inventory_stock_non_negative CHECK (current_stock >= 0)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_inventory_thresholds' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE inventory ADD CONSTRAINT chk_inventory_thresholds CHECK (min_stock >= 0 AND max_stock >= min_stock)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 税率约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_tax_rate_range' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE tax_rules ADD CONSTRAINT chk_tax_rate_range CHECK (tax_rate >= 0 AND tax_rate <= 1)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_store_tax_rate_range' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE stores ADD CONSTRAINT chk_store_tax_rate_range CHECK (tax_rate >= 0 AND tax_rate <= 1)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 优惠券约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_coupon_discount' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE coupons ADD CONSTRAINT chk_coupon_discount CHECK ((discount_type = ''FIXED_AMOUNT'' AND discount_value > 0) OR (discount_type = ''PERCENTAGE'' AND discount_value > 0 AND discount_value <= 100))', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 考勤时间约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_attendance_time_order' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE attendance ADD CONSTRAINT chk_attendance_time_order CHECK (clock_out_time IS NULL OR clock_out_time >= clock_in_time)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 会话过期时间约束
SET @sql = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME = 'chk_session_expires' AND TABLE_SCHEMA = DATABASE()) = 0,
    'ALTER TABLE user_sessions ADD CONSTRAINT chk_session_expires CHECK ((access_token_expires_at IS NULL OR access_token_expires_at > created_at) AND (refresh_token_expires_at IS NULL OR refresh_token_expires_at > created_at))', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- =================================
-- 10. 触发器
-- =================================

DELIMITER //

-- 订单金额一致性检查
CREATE TRIGGER trg_order_amount_consistency
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
    DECLARE calculated_total DECIMAL(10,2);

    SELECT IFNULL(SUM(subtotal), 0) INTO calculated_total
    FROM order_items
    WHERE order_id = NEW.order_id AND is_deleted = FALSE;

    IF ABS(calculated_total - (NEW.total_amount - NEW.tax_amount - NEW.tip_amount + NEW.discount_amount)) > calculated_total * 0.05 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '订单总金额与订单项金额不一致';
    END IF;
END//

-- 库存扣减触发器
CREATE TRIGGER trg_inventory_deduction
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE inventory
    SET current_stock = current_stock - NEW.quantity,
        updated_at = CURRENT_TIMESTAMP,
        updated_by = NEW.created_by
    WHERE product_id = NEW.product_id;

    IF (SELECT current_stock FROM inventory WHERE product_id = NEW.product_id) < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '商品库存不足';
    END IF;
END//

-- 优惠券使用次数更新
CREATE TRIGGER trg_coupon_usage_update
AFTER INSERT ON order_coupons
FOR EACH ROW
BEGIN
    UPDATE coupons
    SET used_count = used_count + 1,
        updated_at = CURRENT_TIMESTAMP,
        updated_by = NEW.created_by
    WHERE coupon_id = NEW.coupon_id;

    IF EXISTS (
        SELECT 1 FROM coupons 
        WHERE coupon_id = NEW.coupon_id 
          AND usage_limit > 0 
          AND used_count > usage_limit
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '优惠券使用次数已达上限';
    END IF;
END//

-- 考勤工时自动计算
CREATE TRIGGER trg_attendance_hours_calculation
BEFORE UPDATE ON attendance
FOR EACH ROW
BEGIN
    IF NEW.clock_out_time IS NOT NULL AND OLD.clock_out_time IS NULL THEN
        SET NEW.total_hours = TIMESTAMPDIFF(MINUTE, NEW.clock_in_time, NEW.clock_out_time) / 60.0;
    END IF;
END//

DELIMITER ;

-- =================================
-- 11. 存储过程
-- =================================

DELIMITER //

-- 日销售汇总存储过程
CREATE PROCEDURE sp_generate_daily_sales_report(
    IN p_store_id CHAR(36),
    IN p_report_date DATE
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    DELETE FROM daily_sales_reports
    WHERE store_id = p_store_id AND report_date = p_report_date;

    INSERT INTO daily_sales_reports (
        report_id, store_id, report_date, total_sales_amount, total_orders,
        average_order_value, total_tips, top_product_id, created_by
    )
    SELECT
        UUID(),
        p_store_id,
        p_report_date,
        IFNULL(SUM(o.total_amount), 0) as total_sales_amount,
        COUNT(o.order_id) as total_orders,
        IFNULL(AVG(o.total_amount), 0) as average_order_value,
        IFNULL(SUM(o.tip_amount), 0) as total_tips,
        (SELECT oi.product_id
         FROM order_items oi
         JOIN orders o2 ON oi.order_id = o2.order_id
         WHERE o2.store_id = p_store_id
           AND DATE(o2.created_at) = p_report_date
           AND o2.status = 'COMPLETED'
           AND oi.is_deleted = FALSE
         GROUP BY oi.product_id
         ORDER BY SUM(oi.quantity) DESC
         LIMIT 1) as top_product_id,
        'USR-174000000001' as created_by
    FROM orders o
    WHERE o.store_id = p_store_id
      AND DATE(o.created_at) = p_report_date
      AND o.status = 'COMPLETED'
      AND o.is_deleted = FALSE;

    COMMIT;
END//

-- 库存预警检查存储过程
CREATE PROCEDURE sp_check_inventory_alerts(
    IN p_store_id CHAR(36)
)
BEGIN
    SELECT
        p.product_id as product_id,
        p.product_name,
        i.current_stock,
        i.min_stock,
        c.category_name
    FROM inventory i
    JOIN products p ON i.product_id = p.product_id
    JOIN categories c ON p.category_id = c.category_id
    WHERE p.store_id = p_store_id
      AND i.current_stock <= i.min_stock
      AND p.is_active = TRUE
      AND p.is_deleted = FALSE
    ORDER BY (i.current_stock / NULLIF(i.min_stock, 0));
END//

-- 用户权限检查存储过程
CREATE PROCEDURE sp_check_user_permission(
    IN p_user_id CHAR(36),
    IN p_resource VARCHAR(50),
    IN p_action VARCHAR(50),
    OUT p_has_permission BOOLEAN
)
BEGIN
    DECLARE permission_count INT DEFAULT 0;

    SELECT COUNT(*) INTO permission_count
    FROM users u
    JOIN roles r ON u.role_id = r.role_id
    JOIN role_permissions rp ON r.role_id = rp.role_id
    JOIN permissions p ON rp.permission_id = p.permission_id
    WHERE u.user_id = p_user_id
      AND p.resource = p_resource
      AND p.action = p_action
      AND u.is_deleted = FALSE
      AND r.is_active = TRUE
      AND r.is_deleted = FALSE
      AND p.is_deleted = FALSE;

    SET p_has_permission = (permission_count > 0);
END//

DELIMITER ;

-- =================================
-- 12. 优化视图
-- =================================

-- 用户权限视图
CREATE VIEW v_user_permissions AS
SELECT
    u.user_id,
    u.store_id,
    u.email,
    u.first_name,
    u.last_name,
    r.role_name,
    p.permission_code,
    p.resource,
    p.action
FROM users u
JOIN roles r ON u.role_id = r.role_id AND r.is_active = TRUE
JOIN role_permissions rp ON r.role_id = rp.role_id
JOIN permissions p ON rp.permission_id = p.permission_id
WHERE u.is_deleted = FALSE
  AND r.is_deleted = FALSE
  AND p.is_deleted = FALSE;

-- 商品库存视图
CREATE VIEW v_product_inventory AS
SELECT
    p.product_id,
    p.store_id,
    p.product_name,
    p.price,
    c.category_name,
    i.current_stock,
    i.min_stock,
    i.max_stock,
    i.cost_price,
    CASE
        WHEN i.current_stock <= i.min_stock THEN 'LOW_STOCK'
        WHEN i.current_stock >= i.max_stock THEN 'OVERSTOCK'
        ELSE 'NORMAL'
    END as stock_status,
    p.is_active,
    p.updated_at
FROM products p
JOIN categories c ON p.category_id = c.category_id
JOIN inventory i ON p.product_id = i.product_id
WHERE p.is_deleted = FALSE
  AND c.is_deleted = FALSE
  AND i.is_deleted = FALSE;

-- 订单详情视图
CREATE VIEW v_order_details AS
SELECT
    o.order_id,
    o.store_id,
    o.order_number,
    o.total_amount,
    o.status as order_status,
    o.payment_status,
    o.created_at,
    CONCAT(u.first_name, ' ', u.last_name) as cashier_name,
    c.customer_name,
    c.phone as customer_phone,
    COUNT(oi.order_item_id) as item_count,
    SUM(oi.quantity) as total_quantity
FROM orders o
LEFT JOIN users u ON o.user_id = u.user_id
LEFT JOIN customers c ON o.customer_id = c.customer_id
LEFT JOIN order_items oi ON o.order_id = oi.order_id AND oi.is_deleted = FALSE
WHERE o.is_deleted = FALSE
GROUP BY o.order_id, o.store_id, o.order_number, o.total_amount,
         o.status, o.payment_status, o.created_at,
         u.first_name, u.last_name, c.customer_name, c.phone;

-- 慢查询监控视图
CREATE VIEW v_slow_queries AS
SELECT
    DIGEST_TEXT as query_text,
    COUNT_STAR as exec_count,
    AVG_TIMER_WAIT/1000000000000 as avg_time_sec,
    MAX_TIMER_WAIT/1000000000000 as max_time_sec,
    SUM_ROWS_EXAMINED as total_rows_examined,
    SUM_ROWS_SENT as total_rows_sent,
    FIRST_SEEN,
    LAST_SEEN
FROM performance_schema.events_statements_summary_by_digest
WHERE AVG_TIMER_WAIT > 2000000000000 -- 大于2秒的查询
ORDER BY AVG_TIMER_WAIT DESC
LIMIT 20;

-- 表空间使用情况视图
CREATE VIEW v_table_space_usage AS
SELECT
    TABLE_SCHEMA as database_name,
    TABLE_NAME as table_name,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) as size_mb,
    TABLE_ROWS as row_count,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / TABLE_ROWS), 2) as avg_row_size,
    ENGINE
FROM information_schema.TABLES
WHERE TABLE_SCHEMA NOT IN ('information_schema', 'mysql', 'performance_schema', 'sys')
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;

-- 索引使用情况视图
CREATE VIEW v_index_usage AS
SELECT
    OBJECT_SCHEMA as database_name,
    OBJECT_NAME as table_name,
    INDEX_NAME as index_name,
    COUNT_FETCH as select_count,
    COUNT_INSERT as insert_count,
    COUNT_UPDATE as update_count,
    COUNT_DELETE as delete_count
FROM performance_schema.table_io_waits_summary_by_index_usage
WHERE OBJECT_SCHEMA NOT IN ('information_schema', 'mysql', 'performance_schema', 'sys')
ORDER BY COUNT_FETCH DESC;

-- =================================
-- 13. 初始化基础数据
-- =================================

-- ===========================
-- 插入角色表
-- ===========================
INSERT INTO roles (role_id, role_name, role_code, description) VALUES
('ROL-00001', 'Cashier', 'CASHIER', 'Front-line staff handling transactions and customer service'),
('ROL-00002', 'Server', 'SERVER', 'Service staff taking orders and serving customers'),
('ROL-00003', 'Manager', 'MANAGER', 'Store-level management with operational oversight'),
('ROL-00004', 'Admin', 'ADMIN', 'Full system access with administrative privileges');

-- ===========================
-- 插入权限表
-- ===========================
INSERT INTO permissions (permission_id, permission_name, permission_code, resource, action, description) VALUES
('PERM-00001', 'Process Sales Transactions', 'PROCESS_SALES', 'SALES', 'CREATE', '处理销售交易'),
('PERM-00002', 'Handle Payment Processing', 'PAYMENT_PROCESS', 'PAYMENT', 'HANDLE', '处理支付'),
('PERM-00003', 'Issue Customer Receipts', 'ISSUE_RECEIPT', 'RECEIPT', 'ISSUE', '打印客户小票'),
('PERM-00004', 'Apply Basic Discounts (up to 15%)', 'DISCOUNT_BASIC', 'SALES', 'DISCOUNT_BASIC', '应用基础折扣'),
('PERM-00005', 'Process Returns (up to $100)', 'RETURN_BASIC', 'SALES', 'RETURN', '处理退货'),
('PERM-00006', 'View Inventory Levels', 'VIEW_INVENTORY', 'INVENTORY', 'VIEW', '查看库存'),
('PERM-00007', 'Access Daily Sales Reports', 'VIEW_SALES_REPORTS', 'SALES_REPORT', 'VIEW', '访问日报表'),
('PERM-00008', 'Manage Cash Drawer', 'MANAGE_CASH_DRAWER', 'CASH_DRAWER', 'MANAGE', '管理钱箱'),
('PERM-00009', 'Take Customer Orders', 'TAKE_ORDERS', 'ORDER', 'CREATE', '接单'),
('PERM-00010', 'Apply Discounts (up to 10%)', 'DISCOUNT_SERVER', 'SALES', 'DISCOUNT_SERVER', '服务员折扣'),
('PERM-00011', 'Modify Order Items', 'MODIFY_ORDER', 'ORDER', 'EDIT', '修改订单项'),
('PERM-00012', 'View Menu Items & Prices', 'VIEW_MENU', 'MENU', 'VIEW', '查看菜单'),
('PERM-00013', 'Access Table Management', 'TABLE_MANAGEMENT', 'TABLE', 'MANAGE', '管理桌台'),
('PERM-00014', 'View Inventory Status', 'VIEW_INVENTORY_STATUS', 'INVENTORY', 'VIEW_STATUS', '查看库存状态'),
('PERM-00015', 'Print Customer Receipts', 'PRINT_RECEIPT', 'RECEIPT', 'PRINT', '打印小票'),
('PERM-00016', 'Void/Cancel Transactions', 'VOID_TRANSACTION', 'SALES', 'VOID', '作废交易'),
('PERM-00017', 'Process Refunds (up to $500)', 'REFUND_LIMIT_500', 'SALES', 'REFUND', '处理退款'),
('PERM-00018', 'Apply Manager Discounts (up to 30%)', 'DISCOUNT_MANAGER', 'SALES', 'DISCOUNT_MANAGER', '经理折扣'),
('PERM-00019', 'Manage Inventory Adjustments', 'INVENTORY_ADJUST', 'INVENTORY', 'MANAGE', '库存调整'),
('PERM-00020', 'View Financial Reports', 'VIEW_FINANCIAL_REPORTS', 'FINANCE', 'VIEW', '查看财务报表'),
('PERM-00021', 'Manage Staff Schedules', 'STAFF_SCHEDULING', 'STAFF', 'MANAGE', '管理员工排班'),
('PERM-00022', 'Access Performance Analytics', 'PERFORMANCE_ANALYTICS', 'PERFORMANCE', 'VIEW', '访问绩效分析'),
('PERM-00023', 'Override Transaction Limits', 'OVERRIDE_LIMITS', 'SALES', 'OVERRIDE', '覆盖交易限制'),
('PERM-00024', 'Backup & Restore Operations', 'BACKUP_OPERATIONS', 'SYSTEM', 'BACKUP', '备份与恢复操作'),
('PERM-00025', 'System Configuration', 'SYSTEM_CONFIG', 'SYSTEM', 'CONFIGURE', '系统配置'),
('PERM-00026', 'User Account Management', 'USER_MANAGEMENT', 'USER', 'MANAGE', '用户管理'),
('PERM-00027', 'Role & Permission Management', 'ROLE_PERMISSION_MGMT', 'ROLE', 'MANAGE', '角色权限管理'),
('PERM-00028', 'Financial Data Access', 'FINANCIAL_DATA_ACCESS', 'FINANCE', 'ACCESS', '访问财务数据'),
('PERM-00029', 'Audit Log Access', 'AUDIT_LOG', 'SYSTEM', 'VIEW', '审计日志访问'),
('PERM-00030', 'Database Management', 'DB_MANAGEMENT', 'DATABASE', 'MANAGE', '数据库管理'),
('PERM-00031', 'Security Settings', 'SECURITY_SETTINGS', 'SYSTEM', 'SECURITY', '安全设置'),
('PERM-00032', 'Integration Management', 'INTEGRATION_MGMT', 'SYSTEM', 'INTEGRATE', '系统集成管理'),
('PERM-00033', 'Backup Administration', 'BACKUP_ADMIN', 'SYSTEM', 'BACKUP_ADMIN', '备份管理'),
('PERM-00034', 'System Monitoring', 'SYSTEM_MONITORING', 'SYSTEM', 'MONITOR', '系统监控'),
('PERM-00035', 'Advanced Reporting', 'ADVANCED_REPORTING', 'REPORT', 'VIEW', '高级报表');

-- ===========================
-- Note: Sample users will be inserted after merchants and stores are created
-- to avoid foreign key constraint violations
-- ===========================

-- ===========================
-- 插入角色权限关联
-- ===========================

-- Cashier 权限绑定
INSERT INTO role_permissions (role_id, permission_id, granted_at) VALUES
('ROL-00001', 'PERM-00001', NOW()), -- Process Sales Transactions
('ROL-00001', 'PERM-00002', NOW()), -- Handle Payment Processing
('ROL-00001', 'PERM-00003', NOW()), -- Issue Customer Receipts
('ROL-00001', 'PERM-00004', NOW()), -- Apply Basic Discounts (up to 15%)
('ROL-00001', 'PERM-00005', NOW()), -- Process Returns (up to $100)
('ROL-00001', 'PERM-00006', NOW()), -- View Inventory Levels
('ROL-00001', 'PERM-00007', NOW()), -- Access Daily Sales Reports
('ROL-00001', 'PERM-00008', NOW()); -- Manage Cash Drawer

-- Server 权限绑定
INSERT INTO role_permissions (role_id, permission_id, granted_at) VALUES
('ROL-00002', 'PERM-00009', NOW()), -- Take Customer Orders
('ROL-00002', 'PERM-00001', NOW()), -- Process Sales Transactions
('ROL-00002', 'PERM-00010', NOW()), -- Apply Discounts (up to 10%)
('ROL-00002', 'PERM-00011', NOW()), -- Modify Order Items
('ROL-00002', 'PERM-00012', NOW()), -- View Menu Items & Prices
('ROL-00002', 'PERM-00013', NOW()), -- Access Table Management
('ROL-00002', 'PERM-00014', NOW()), -- View Inventory Status
('ROL-00002', 'PERM-00015', NOW()); -- Print Customer Receipts

-- Manager 权限绑定 (Cashier + Server + Manager专属)
INSERT INTO role_permissions (role_id, permission_id, granted_at) VALUES
-- Cashier 权限
('ROL-00003', 'PERM-00001', NOW()),
('ROL-00003', 'PERM-00002', NOW()),
('ROL-00003', 'PERM-00003', NOW()),
('ROL-00003', 'PERM-00004', NOW()),
('ROL-00003', 'PERM-00005', NOW()),
('ROL-00003', 'PERM-00006', NOW()),
('ROL-00003', 'PERM-00007', NOW()),
('ROL-00003', 'PERM-00008', NOW()),
-- Server 权限
('ROL-00003', 'PERM-00009', NOW()),
('ROL-00003', 'PERM-00010', NOW()),
('ROL-00003', 'PERM-00011', NOW()),
('ROL-00003', 'PERM-00012', NOW()),
('ROL-00003', 'PERM-00013', NOW()),
('ROL-00003', 'PERM-00014', NOW()),
('ROL-00003', 'PERM-00015', NOW()),
-- Manager 专属权限
('ROL-00003', 'PERM-00016', NOW()), -- Void/Cancel Transactions
('ROL-00003', 'PERM-00017', NOW()), -- Process Refunds (up to $500)
('ROL-00003', 'PERM-00018', NOW()), -- Apply Manager Discounts (up to 30%)
('ROL-00003', 'PERM-00019', NOW()), -- Manage Inventory Adjustments
('ROL-00003', 'PERM-00020', NOW()), -- View Financial Reports
('ROL-00003', 'PERM-00021', NOW()), -- Manage Staff Schedules
('ROL-00003', 'PERM-00022', NOW()), -- Access Performance Analytics
('ROL-00003', 'PERM-00023', NOW()), -- Override Transaction Limits
('ROL-00003', 'PERM-00024', NOW()); -- Backup & Restore Operations

-- Admin 权限绑定 (Manager + Admin专属)
INSERT INTO role_permissions (role_id, permission_id, granted_at) VALUES
-- Manager 权限
('ROL-00004', 'PERM-00001', NOW()),
('ROL-00004', 'PERM-00002', NOW()),
('ROL-00004', 'PERM-00003', NOW()),
('ROL-00004', 'PERM-00004', NOW()),
('ROL-00004', 'PERM-00005', NOW()),
('ROL-00004', 'PERM-00006', NOW()),
('ROL-00004', 'PERM-00007', NOW()),
('ROL-00004', 'PERM-00008', NOW()),
('ROL-00004', 'PERM-00009', NOW()),
('ROL-00004', 'PERM-00010', NOW()),
('ROL-00004', 'PERM-00011', NOW()),
('ROL-00004', 'PERM-00012', NOW()),
('ROL-00004', 'PERM-00013', NOW()),
('ROL-00004', 'PERM-00014', NOW()),
('ROL-00004', 'PERM-00015', NOW()),
('ROL-00004', 'PERM-00016', NOW()),
('ROL-00004', 'PERM-00017', NOW()),
('ROL-00004', 'PERM-00018', NOW()),
('ROL-00004', 'PERM-00019', NOW()),
('ROL-00004', 'PERM-00020', NOW()),
('ROL-00004', 'PERM-00021', NOW()),
('ROL-00004', 'PERM-00022', NOW()),
('ROL-00004', 'PERM-00023', NOW()),
('ROL-00004', 'PERM-00024', NOW()),
-- Admin 专属权限
('ROL-00004', 'PERM-00025', NOW()), -- System Configuration
('ROL-00004', 'PERM-00026', NOW()), -- User Account Management
('ROL-00004', 'PERM-00027', NOW()), -- Role & Permission Management
('ROL-00004', 'PERM-00028', NOW()), -- Financial Data Access
('ROL-00004', 'PERM-00029', NOW()), -- Audit Log Access
('ROL-00004', 'PERM-00030', NOW()), -- Database Management
('ROL-00004', 'PERM-00031', NOW()), -- Security Settings
('ROL-00004', 'PERM-00032', NOW()), -- Integration Management
('ROL-00004', 'PERM-00033', NOW()), -- Backup Administration
('ROL-00004', 'PERM-00034', NOW()), -- System Monitoring
('ROL-00004', 'PERM-00035', NOW()); -- Advanced Reporting


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
INSERT INTO users (user_id, merchant_id, store_id, username, email, password_hash, first_name, last_name, role_id, status, salary, hire_date) VALUES
('USR-174000000001', 'MRC-174000000001', 'LOC-174000000001', 'admin', 'admin@trip7cafe.com', '$2a$10$rMgr4YOhVNcXP7Qhp8jQHe7vKbQZ8tXkF2VyLjKrX3nP9sWqR1tGu', 'Admin', 'User', 'ROL-00004', 'ACTIVE', 50000.00, '2024-01-01');

-- 示例员工用户（使用正确的merchant_id和store_id）
INSERT INTO users (user_id, merchant_id, store_id, username, email, password_hash, first_name, last_name, role_id, status, salary, hire_date, last_login_at) VALUES
('USR-174000000002', 'MRC-174000000001', 'LOC-174000000001', 'cashier_john', 'cashier.john@trip7cafe.com', '$2a$10$hashedpassword111', 'John', 'Doe', 'ROL-00001', 'ACTIVE', 3000.00, '2023-05-01', NOW()),
('USR-174000000003', 'MRC-174000000001', 'LOC-174000000001', 'server_alice', 'alice.server@trip7cafe.com', '$2a$10$hashedpassword222', 'Alice', 'Smith', 'ROL-00002', 'ACTIVE', 3200.00, '2023-06-10', NOW()),
('USR-174000000004', 'MRC-174000000001', 'LOC-174000000001', 'manager_bob', 'bob.manager@trip7cafe.com', '$2a$10$hashedpassword333', 'Bob', 'Lee', 'ROL-00003', 'ACTIVE', 5000.00, '2022-11-20', NOW());

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

-- 示例税务规则
INSERT INTO tax_rules (tax_rule_id, store_id, tax_name, tax_rate, tax_type, applicable_to, effective_from, created_by) VALUES
('TAX-174000000001', 'LOC-174000000001', '纽约州税', 0.0400, 'STATE_TAX', 'ALL', '2024-01-01', 'MRC-174000000001'),
('TAX-174000000002', 'LOC-174000000001', '纽约市税', 0.0475, 'CITY_TAX', 'ALL', '2024-01-01', 'MRC-174000000001');

-- 示例设备
INSERT INTO devices (device_id, store_id, device_name, device_type, status, created_by) VALUES
('DEV-174000000001', 'LOC-174000000001', 'POS Terminal 1', 'POS_TERMINAL', 'ONLINE', 'MRC-174000000001'),
('DEV-174000000002', 'LOC-174000000001', 'Kitchen Display 1', 'KITCHEN_DISPLAY', 'ONLINE', 'MRC-174000000001'),
('DEV-174000000003', 'LOC-174000000001', 'Receipt Printer 1', 'RECEIPT_PRINTER', 'ONLINE', 'MRC-174000000001');

-- 插入性能基准数据（使用REPLACE避免重复主键错误）
REPLACE INTO performance_metrics (metric_id, metric_name, metric_value, measured_at) VALUES
('MET-174000000001', 'baseline_order_insert_time_ms', 50.0, NOW()),
('MET-174000000002', 'baseline_product_search_time_ms', 20.0, NOW()),
('MET-174000000003', 'baseline_payment_process_time_ms', 100.0, NOW()),
('MET-174000000004', 'baseline_report_generation_time_ms', 500.0, NOW());

-- =================================
-- 14. 性能调优配置
-- =================================

-- 查询缓存和连接优化
SET GLOBAL max_connections = 1000;
SET GLOBAL max_user_connections = 100;
SET GLOBAL connect_timeout = 30;
SET GLOBAL wait_timeout = 28800;
SET GLOBAL interactive_timeout = 28800;

-- 查询优化参数
SET GLOBAL tmp_table_size = 64 * 1024 * 1024; -- 64MB
SET GLOBAL max_heap_table_size = 64 * 1024 * 1024; -- 64MB
SET GLOBAL sort_buffer_size = 2 * 1024 * 1024; -- 2MB
SET GLOBAL read_buffer_size = 1 * 1024 * 1024; -- 1MB

-- InnoDB存储引擎优化
-- SET GLOBAL innodb_buffer_pool_instances = 8; -- 只读变量，需在配置文件中设置
-- SET GLOBAL innodb_buffer_pool_chunk_size = 134217728; -- 只读变量，需在配置文件中设置
SET GLOBAL innodb_log_buffer_size = 16777216; -- 16MB
SET GLOBAL innodb_flush_log_at_trx_commit = 1; -- 数据安全优先
SET GLOBAL innodb_thread_concurrency = 16;
-- SET GLOBAL innodb_read_io_threads = 8; -- 只读变量，需在配置文件中设置
-- SET GLOBAL innodb_write_io_threads = 8; -- 只读变量，需在配置文件中设置
-- SET GLOBAL innodb_file_per_table = ON; -- 可能为只读变量，建议在配置文件中设置
SET GLOBAL innodb_autoextend_increment = 64; -- 64MB自动扩展

-- 慢查询监控配置
SET GLOBAL slow_query_log = ON;
SET GLOBAL long_query_time = 2.0; -- 2秒以上记录慢查询
SET GLOBAL log_queries_not_using_indexes = ON;
SET GLOBAL min_examined_row_limit = 1000;

-- 性能模式配置
UPDATE performance_schema.setup_instruments
SET ENABLED = 'YES', TIMED = 'YES'
WHERE NAME LIKE 'statement/sql/%';

UPDATE performance_schema.setup_consumers
SET ENABLED = 'YES'
WHERE NAME IN (
    'events_statements_current',
    'events_statements_history',
    'events_statements_summary_by_digest'
);

-- 表缓存配置
SET GLOBAL table_open_cache = 4000;
SET GLOBAL table_definition_cache = 2000;
SET GLOBAL thread_cache_size = 100;
SET GLOBAL key_buffer_size = 16777216; -- 16MB

-- 启用事件调度器
SET GLOBAL event_scheduler = ON;

-- =================================
-- 15. 自动化事件调度
-- =================================

DELIMITER //

-- 每日销售报表自动生成
CREATE EVENT IF NOT EXISTS ev_daily_sales_report
ON SCHEDULE EVERY 1 DAY
STARTS (CURDATE() + INTERVAL 1 DAY + INTERVAL 1 HOUR)
DO
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_store_id CHAR(36);
    DECLARE store_cursor CURSOR FOR
        SELECT id FROM stores WHERE status = 'ACTIVE' AND is_deleted = FALSE;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN store_cursor;
    store_loop: LOOP
        FETCH store_cursor INTO v_store_id;
        IF done THEN
            LEAVE store_loop;
        END IF;

        CALL sp_generate_daily_sales_report(v_store_id, CURDATE() - INTERVAL 1 DAY);
    END LOOP;
    CLOSE store_cursor;
END//

-- 每小时库存预警检查
CREATE EVENT IF NOT EXISTS ev_inventory_alert_check
ON SCHEDULE EVERY 1 HOUR
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_store_id CHAR(36);
    DECLARE store_cursor CURSOR FOR
        SELECT id FROM stores WHERE status = 'ACTIVE' AND is_deleted = FALSE;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN store_cursor;
    store_loop: LOOP
        FETCH store_cursor INTO v_store_id;
        IF done THEN
            LEAVE store_loop;
        END IF;

        INSERT INTO notifications (notification_id, store_id, title, message, type, created_by)
        SELECT
            UUID(),
            v_store_id,
            '库存预警',
            CONCAT('商品 "', p.product_name, '" 库存不足，当前库存：', i.current_stock, '，最低库存：', i.min_stock),
            'INVENTORY_ALERT',
            'USR-174000000001'
        FROM inventory i
        JOIN products p ON i.product_id = p.product_id
        WHERE p.store_id = v_store_id
          AND i.current_stock <= i.min_stock
          AND p.is_active = TRUE
          AND p.is_deleted = FALSE
          AND i.is_deleted = FALSE;
    END LOOP;
    CLOSE store_cursor;
END//

-- 清理过期会话
CREATE EVENT IF NOT EXISTS ev_cleanup_expired_sessions
ON SCHEDULE EVERY 1 HOUR
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    DELETE FROM user_sessions
    WHERE (access_token_expires_at < NOW() - INTERVAL 1 DAY)
       OR (refresh_token_expires_at < NOW() - INTERVAL 7 DAY)
       OR (status = 'INACTIVE' AND updated_at < NOW() - INTERVAL 1 DAY);
END//

-- 清理已读通知 (保留30天)
CREATE EVENT IF NOT EXISTS ev_cleanup_old_notifications
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURDATE() + INTERVAL 1 DAY, '04:00:00')
DO
BEGIN
    DELETE FROM notifications
    WHERE is_read = TRUE
      AND read_at < NOW() - INTERVAL 30 DAY;
END//

-- 数据库连接数监控
CREATE EVENT IF NOT EXISTS ev_monitor_connections
ON SCHEDULE EVERY 5 MINUTE
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    DECLARE current_connections INT;
    DECLARE max_connections_limit INT;

    SELECT VARIABLE_VALUE INTO current_connections
    FROM performance_schema.global_status
    WHERE VARIABLE_NAME = 'Threads_connected';

    SELECT @@max_connections INTO max_connections_limit;

    IF current_connections > max_connections_limit * 0.8 THEN
        INSERT INTO system_alerts (alert_id, alert_type, alert_level, alert_message, alert_data)
        VALUES (
            UUID(),
            'HIGH_CONNECTIONS', 
            'WARNING',
            CONCAT('数据库连接数过高: ', current_connections, '/', max_connections_limit),
            JSON_OBJECT('current_connections', current_connections, 'max_connections', max_connections_limit)
        );
    END IF;
END//

-- 每周统计信息更新
CREATE EVENT IF NOT EXISTS ev_weekly_stats_update
ON SCHEDULE EVERY 1 WEEK
STARTS (CURDATE() + INTERVAL (7 - WEEKDAY(CURDATE())) DAY + INTERVAL 3 HOUR)
DO
BEGIN
    ANALYZE TABLE stores, users, products, categories, inventory,
                 orders, order_items, payments, customers, coupons;

    INSERT INTO performance_metrics (metric_id, metric_name, metric_value, measured_at)
    SELECT
        CONCAT('MET-', UNIX_TIMESTAMP(NOW()), '-001'),
        'avg_order_processing_time',
        AVG(TIMESTAMPDIFF(SECOND, created_at, updated_at)),
        NOW()
    FROM orders
    WHERE created_at >= CURDATE() - INTERVAL 7 DAY
      AND status = 'COMPLETED';

    INSERT INTO performance_metrics (metric_id, metric_name, metric_value, measured_at)
    SELECT
        CONCAT('MET-', UNIX_TIMESTAMP(NOW()), '-002'),
        'daily_order_count',
        COUNT(*) / 7.0,
        NOW()
    FROM orders
    WHERE created_at >= CURDATE() - INTERVAL 7 DAY;
END//

DELIMITER ;

-- =================================
-- 16. 安全和权限优化
-- =================================

-- 创建只读用户 (用于报表查询)
CREATE USER IF NOT EXISTS 'pos_readonly'@'%' IDENTIFIED BY 'ReadOnly@2024!';
GRANT SELECT ON pos_db.* TO 'pos_readonly'@'%';
GRANT SELECT ON performance_schema.* TO 'pos_readonly'@'%';

-- 创建备份用户
CREATE USER IF NOT EXISTS 'pos_backup'@'localhost' IDENTIFIED BY 'Backup@2024!';
GRANT SELECT, LOCK TABLES, SHOW VIEW, EVENT, TRIGGER ON pos_db.* TO 'pos_backup'@'localhost';

-- 创建应用用户 (限制权限)
CREATE USER IF NOT EXISTS 'pos_app'@'%' IDENTIFIED BY 'PosApp@2024!';
GRANT SELECT, INSERT, UPDATE, DELETE ON pos_db.* TO 'pos_app'@'%';
GRANT EXECUTE ON pos_db.* TO 'pos_app'@'%';

-- 完成POS系统数据库初始化

