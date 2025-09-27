# POS系统数据库迁移文件

## 概述

此目录包含POS系统的完整数据库迁移文件，采用统一文件设计，便于执行和维护。所有数据库结构、初始数据、优化配置都整合在一个文件中。

## 文件结构

### 主要文件
- **V1__create_pos_system.sql** - 完整的POS系统数据库设计（统一版本）

## 主要特性

### 1. 统一文件设计
- 所有数据库结构、初始数据、优化配置都在一个文件中
- 便于一次性执行和部署
- 减少了文件管理的复杂性

### 2. 问题修复
- **混合数据类型**：统一使用CHAR(36) UUID作为主键
- **货币一致性**：统一使用USD作为默认货币
- **文件大小**：通过清晰的分模块注释保持可读性

### 3. 性能优化
- 添加了复合索引优化查询性能
- 配置了数据库性能参数
- 实现了JSON字段的虚拟列索引

## 使用方法

### 执行完整迁移
```bash
# 执行完整的POS系统数据库初始化
mysql -u username -p database_name < V1__create_pos_system.sql
```

### 使用Docker Compose
```bash
# 如果使用Docker Compose，数据库会自动执行迁移文件
docker-compose up -d
```

## 数据库结构概览

### 1. 商家与门店管理模块
- **merchants** - 商家表（Square风格，UUID主键）
- **merchant_bank_accounts** - 商家银行账户表
- **stores** - 门店表（UUID主键）
- **users** - 用户表（门店员工，UUID外键）

### 2. 权限管理模块
- **roles** - 角色表
- **permissions** - 权限表
- **user_roles** - 用户角色关联表
- **role_permissions** - 角色权限关联表

### 3. 商品与库存管理模块
- **categories** - 商品分类表
- **products** - 商品表
- **inventory** - 库存表
- **menu_categories** - 菜单分类表
- **menu_items** - 菜单项表

### 4. 订单与支付管理模块
- **customers** - 客户表
- **orders** - 订单表
- **order_items** - 订单明细表
- **payments** - 支付表
- **coupons** - 优惠券表
- **order_coupons** - 订单优惠券关联表

### 5. 考勤与会话管理模块
- **attendance** - 考勤表
- **user_sessions** - 用户会话表

### 6. 系统管理与配置模块
- **devices** - 设备表
- **tax_rules** - 税务规则表
- **notifications** - 通知表

### 7. 报表与监控模块
- **daily_sales_reports** - 日销售汇总表
- **performance_metrics** - 性能监控表

## 核心功能

### 数据完整性
- 所有表都包含软删除字段(`is_deleted`)
- 完整的审计字段(`created_at`, `updated_at`, `created_by`, `updated_by`)
- 外键约束确保数据一致性

### 性能优化
- 复合索引优化常用查询
- 虚拟列索引优化JSON字段查询
- 数据库参数调优配置

### 业务约束
- 价格必须为正数
- 库存不能为负数
- 税率在0-1之间
- 优惠券折扣值验证
- 考勤时间逻辑验证

### 初始数据
- 完整的权限和角色体系
- 示例商家和门店数据
- 测试商品和库存数据
- 管理员用户和权限分配

## 技术规范

### 数据库配置
- **存储引擎**: InnoDB
- **字符集**: utf8mb4
- **排序规则**: utf8mb4_unicode_ci
- **主键格式**: CHAR(36) UUID

### 命名规范
- 表名使用下划线分隔的小写字母
- 字段名使用下划线分隔的小写字母
- 索引名以`idx_`开头
- 外键名以`fk_`开头
- 唯一键名以`uk_`开头

### 数据类型
- **主键**: CHAR(36) UUID
- **金额**: DECIMAL(10,2) 或 DECIMAL(12,2)
- **数量**: INT
- **时间**: TIMESTAMP
- **日期**: DATE
- **布尔值**: BOOLEAN
- **文本**: VARCHAR 或 TEXT
- **JSON**: JSON

## 维护建议

1. **新增功能**：在相应模块中添加新的表结构
2. **修改现有表**：创建新的迁移文件，不要直接修改现有文件
3. **数据更新**：使用单独的SQL文件进行数据迁移
4. **性能调优**：在性能优化模块中维护相关配置

## 注意事项

- 所有主键和外键都使用CHAR(36) UUID格式
- 统一使用USD作为默认货币
- 所有表都包含软删除字段(`is_deleted`)
- 包含完整的审计字段
- 使用InnoDB存储引擎和utf8mb4字符集
- 文件包含完整的初始数据和性能优化配置

## 快速开始

1. 确保MySQL 8.0+已安装并运行
2. 创建数据库：`CREATE DATABASE pos_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
3. 执行迁移：`mysql -u username -p pos_system < V1__create_pos_system.sql`
4. 验证安装：检查表结构和初始数据是否正确创建

## 环境要求

- **数据库**: MySQL 8.0+ 或 MariaDB 10.3+
- **字符集**: utf8mb4（支持完整Unicode）
- **存储引擎**: InnoDB
- **权限**: 需要CREATE、ALTER、INSERT、INDEX等权限

## 故障排除

### 常见问题
1. **字符集问题**: 确保数据库使用utf8mb4字符集
2. **权限问题**: 确保用户有足够的数据库权限
3. **外键约束**: 如果遇到外键约束错误，检查表创建顺序

### 验证安装
```sql
-- 检查表是否创建成功
SHOW TABLES;

-- 检查初始数据
SELECT COUNT(*) FROM merchants;
SELECT COUNT(*) FROM permissions;
SELECT COUNT(*) FROM roles;
```