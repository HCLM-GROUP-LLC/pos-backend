# POS系统数据库E-R图设计

## 设计概述

基于Dashboard后台管理需求，采用Monty风格的数据库设计理念，以数据完整性为核心，兼顾查询性能的多租户POS系统数据模型。

## E-R图

```
                    POS System Database E-R Diagram

┌─────────────────────────────────────────────────────────────────────────────┐
│                              租户与用户管理                                 │
└─────────────────────────────────────────────────────────────────────────────┘

    Store (店铺)                    User (用户/员工)               Role (角色)
┌─────────────────┐              ┌─────────────────┐           ┌─────────────────┐
│ store_id (PK)   │─────1:N─────│ user_id (PK)    │──N:M─────│ role_id (PK)    │
│ store_name      │              │ store_id (FK)   │           │ role_name       │
│ address         │              │ email           │           │ role_code       │
│ phone           │              │ password_hash   │           │ description     │
│ tax_rate        │              │ first_name      │           │ is_active       │
│ currency        │              │ last_name       │           │ created_at      │
│ timezone        │              │ status          │           │ updated_at      │
│ business_hours  │              │ salary          │           │ created_by      │
│ created_at      │              │ hire_date       │           │ updated_by      │
│ updated_at      │              │ created_at      │           │ is_deleted      │
│ created_by      │              │ updated_at      │           └─────────────────┘
│ updated_by      │              │ created_by      │
│ is_active       │              │ updated_by      │           Permission (权限)
│ is_deleted      │              │ last_login_at   │           ┌─────────────────┐
└─────────────────┘              │ is_deleted      │           │ permission_id(PK)│
                                 └─────────────────┘           │ permission_name │
                                                               │ permission_code │
    UserRole (用户角色关联)                                     │ resource        │
┌─────────────────┐                                           │ action          │
│ user_id (FK)    │                                           │ description     │
│ role_id (FK)    │                                           │ created_at      │
│ assigned_at     │              RolePermission (角色权限关联) │ updated_at      │
│ assigned_by     │              ┌─────────────────┐           │ created_by      │
│ is_active       │              │ role_id (FK)    │           │ updated_by      │
└─────────────────┘              │ permission_id(FK)│          │ is_deleted      │
                                 │ granted_at      │           └─────────────────┘
                                 │ granted_by      │
                                 └─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              商品与库存管理                                 │
└─────────────────────────────────────────────────────────────────────────────┘

    Category (分类)                Product (商品)               Inventory (库存)
┌─────────────────┐              ┌─────────────────┐           ┌─────────────────┐
│ category_id(PK) │─────1:N─────│ product_id (PK) │──1:1─────│ inventory_id(PK)│
│ store_id (FK)   │              │ store_id (FK)   │           │ product_id (FK) │
│ category_name   │              │ category_id(FK) │           │ current_stock   │
│ description     │              │ product_name    │           │ min_stock       │
│ display_order   │              │ description     │           │ max_stock       │
│ is_active       │              │ price           │           │ cost_price      │
│ created_at      │              │ image_url       │           │ last_updated    │
│ updated_at      │              │ is_active       │           │ created_at      │
│ created_by      │              │ created_at      │           │ updated_at      │
│ updated_by      │              │ updated_at      │           │ created_by      │
│ is_deleted      │              │ created_by      │           │ updated_by      │
└─────────────────┘              │ updated_by      │           │ is_deleted      │
                                 │ is_deleted      │           └─────────────────┘
                                 └─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              订单与支付管理                                 │
└─────────────────────────────────────────────────────────────────────────────┘

    Customer (客户)                Order (订单)                 OrderItem (订单项)
┌─────────────────┐              ┌─────────────────┐           ┌─────────────────┐
│ customer_id(PK) │─────1:N─────│ order_id (PK)   │──1:N─────│ order_item_id(PK)│
│ store_id (FK)   │              │ store_id (FK)   │           │ order_id (FK)   │
│ customer_name   │              │ customer_id(FK) │           │ product_id (FK) │
│ phone           │              │ user_id (FK)    │           │ quantity        │
│ email           │              │ order_number    │           │ unit_price      │
│ points_balance  │              │ idempotency_key │           │ subtotal        │
│ membership_level│              │ total_amount    │           │ created_at      │
│ created_at      │              │ tax_amount      │           │ created_by      │
│ updated_at      │              │ tip_amount      │           │ updated_at      │
│ created_by      │              │ discount_amount │           │ updated_by      │
│ updated_by      │              │ payment_status  │           │ is_deleted      │
│ is_deleted      │              │ order_status    │           └─────────────────┘
└─────────────────┘              │ order_type      │
                                 │ created_at      │           Payment (支付)
    Coupon (优惠券)               │ updated_at      │           ┌─────────────────┐
┌─────────────────┐              │ created_by      │──1:N─────│ payment_id (PK) │
│ coupon_id (PK)  │──N:M────────│ updated_by      │           │ order_id (FK)   │
│ store_id (FK)   │              │ completed_at    │           │ idempotency_key │
│ coupon_code     │              │ is_deleted      │           │ payment_method  │
│ discount_type   │              └─────────────────┘           │ amount          │
│ discount_value  │                                            │ transaction_id  │
│ min_order_amount│              OrderCoupon (关联表)          │ status          │
│ valid_from      │              ┌─────────────────┐           │ processed_at    │
│ valid_until     │              │ order_id (FK)   │           │ created_at      │
│ usage_limit     │              │ coupon_id (FK)  │           │ updated_at      │
│ used_count      │              │ discount_applied│           │ created_by      │
│ is_active       │              │ created_at      │           │ updated_by      │
│ created_at      │              │ created_by      │           │ is_deleted      │
│ updated_at      │              └─────────────────┘           └─────────────────┘
│ created_by      │
│ updated_by      │
│ is_deleted      │
└─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              考勤与会话管理                                 │
└─────────────────────────────────────────────────────────────────────────────┘

    Attendance (考勤)              UserSession (用户会话)       Closing (交班记录)
┌─────────────────┐              ┌─────────────────┐           ┌─────────────────┐
│ attendance_id(PK)│              │ session_id (PK) │           │ closing_id (PK) │
│ user_id (FK)    │              │ user_id (FK)    │           │ user_id (FK)    │
│ store_id (FK)   │              │ device_id (FK)  │           │ store_id (FK)   │
│ clock_in_time   │              │ token_hash      │           │ closing_date    │
│ clock_out_time  │              │ expires_at      │           │ cash_counted    │
│ total_hours     │              │ is_active       │           │ cash_expected   │
│ idempotency_key │              │ created_at      │           │ difference      │
│ sync_status     │              │ updated_at      │           │ sync_status     │
│ created_at      │              │ last_activity   │           │ created_at      │
│ updated_at      │              └─────────────────┘           │ updated_at      │
│ created_by      │                                            │ created_by      │
│ updated_by      │              Receipt (收据记录)             │ updated_by      │
│ is_deleted      │              ┌─────────────────┐           │ is_deleted      │
└─────────────────┘              │ receipt_id (PK) │           └─────────────────┘
                                 │ order_id (FK)   │
                                 │ delivery_method │
                                 │ recipient       │
                                 │ sent_at         │
                                 │ status          │
                                 │ created_at      │
                                 │ updated_at      │
                                 │ created_by      │
                                 │ updated_by      │
                                 │ is_deleted      │
                                 └─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              系统管理与配置                                 │
└─────────────────────────────────────────────────────────────────────────────┘

    Device (设备)                  DeviceCode (激活码)          TaxRule (税务规则)
┌─────────────────┐              ┌─────────────────┐           ┌─────────────────┐
│ device_id (PK)  │─────1:N─────│device_code_id(PK)│          │ tax_rule_id(PK) │
│ store_id (FK)   │              │ device_code(8位) │           │ store_id (FK)   │
│ device_name     │              │ device_id (FK)  │           │ tax_name        │
│ device_type     │              │device_fingerprint│          │ tax_rate        │
│ mac_address     │              │ status(VARCHAR) │           │ tax_type        │
│ ip_address      │              │activation_attempts│         │ applicable_to   │
│ last_online     │              │ max_attempts    │           │ effective_from  │
│ status          │              │ issued_at       │           │ effective_until │
│ registered_at   │              │ expired_at      │           │ is_active       │
│ created_at      │              │ bound_at        │           │ created_at      │
│ updated_at      │              │ created_at      │           │ updated_at      │
│ created_by      │              │ updated_at      │           │ created_by      │
│ updated_by      │              │ created_by      │           │ updated_by      │
│ is_deleted      │              │ updated_by      │           │ is_deleted      │
└─────────────────┘              │ is_deleted      │           └─────────────────┘
                                └─────────────────┘
                                Notification (通知)
                                ┌─────────────────┐
                                │notification_id  │
                                │ (PK)            │
                                │ store_id (FK)   │
                                │ user_id (FK)    │
                                │ title           │
                                │ message         │
                                │ type            │
                                │ is_read         │
                                │ created_at      │
                                │ updated_at      │
                                │ created_by      │
                                │ updated_by      │
                                │ read_at         │
                                │ is_deleted      │
                                └─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              报表与汇总表(反范式化)                         │
└─────────────────────────────────────────────────────────────────────────────┘

    DailySalesReport (日销售汇总)
┌─────────────────────────────────────────────────────────────────┐
│ report_id (PK)          │ 用于快速生成Dashboard报表            │
│ store_id (FK)           │ 避免复杂的实时统计查询               │
│ report_date             │ 每日定时任务更新                     │
│ total_sales_amount      │                                      │
│ total_orders            │                                      │
│ average_order_value     │                                      │
│ total_tips              │                                      │
│ top_product_id          │                                      │
│ created_at              │                                      │
│ updated_at              │                                      │
│ created_by              │                                      │
│ updated_by              │                                      │
│ is_deleted              │                                      │
└─────────────────────────────────────────────────────────────────┘
```

## Dashboard功能实现映射

基于Dashboard后台管理需求，以下是各功能模块与数据库表的对应关系：

### 🔐 登录/注册页
- **管理员邮箱/密码登录**: `User`表 - email、password_hash字段
- **忘记密码**: `User`表 - 可扩展重置令牌字段
- **验证码登录**: `User`表 - 可扩展验证码相关字段

### 📊 首页（概览 Dashboard）
- **昨日/本周销售额**: `DailySalesReport`表 - total_sales_amount字段汇总
- **客单价**: `DailySalesReport`表 - average_order_value字段
- **小费统计**: `DailySalesReport`表 - total_tips字段
- **热门商品**: `DailySalesReport`表 - top_product_id关联Product表
- **报表过滤**: 通过store_id和report_date筛选

### 🏪 店铺管理页
- **营业时间**: `Store`表 - business_hours字段(JSON格式)
- **地址信息**: `Store`表 - address字段
- **税率设置**: `Store`表 - tax_rate字段
- **币种设置**: `Store`表 - currency字段
- **时区设置**: `Store`表 - timezone字段

### 🍽️ 菜单管理页
- **分类管理**: `Category`表 - category_name、display_order、is_active
- **菜品管理**: `Product`表 - product_name、description、price、image_url
- **价格设置**: `Product`表 - price字段
- **库存管理**: `Inventory`表 - current_stock、min_stock、max_stock
- **图片上传**: `Product`表 - image_url字段存储图片路径
- **库存不足提醒**: 通过Inventory表的min_stock阈值判断

### 👥 员工管理页
- **员工列表**: `User`表 - 基本信息展示
- **权限管理**: `Role`、`Permission`、`UserRole`、`RolePermission`表组合
- **薪资管理**: `User`表 - salary字段
- **打卡记录**: 可扩展`Attendance`表(未在当前设计中)

### 📈 报表页
- **销售额趋势**: `Order`表按时间维度统计total_amount
- **品类销量**: `OrderItem`关联`Product`和`Category`统计
- **毛利分析**: `OrderItem`的unit_price与`Inventory`的cost_price计算
- **自定义时间范围**: 通过Order表的created_at字段筛选

### 👤 客户/会员管理页
- **客户列表**: `Customer`表 - 基本信息管理
- **积分系统**: `Customer`表 - points_balance字段
- **优惠券管理**: `Coupon`表 - 优惠券创建和管理
- **礼品卡**: 可扩展`GiftCard`表(未在当前设计中)
- **发放优惠券**: 通过Coupon表创建，OrderCoupon表记录使用

### 💰 支付与结算页
- **支付记录**: `Payment`表 - 记录所有支付信息
- **结算账户**: 可扩展`SettlementAccount`表(未在当前设计中)
- **结算周期**: 通过Payment表的processed_at字段按周期统计
- **手续费明细**: Payment表可扩展fee_amount字段
- **对账功能**: 利用Payment表的transaction_id和idempotency_key

### 📱 设备管理页
- **POS终端列表**: `Device`表 - 设备信息管理
- **设备状态**: `Device`表 - status、last_online字段
- **设备绑定**: `Device`表 - mac_address、ip_address字段
- **设备激活**: `DeviceCode`表 - Square风格激活码管理，6-8位短码
- **激活码状态**: `DeviceCode`表 - status字段(UNUSED/BOUND/EXPIRED)
- **设备指纹**: `DeviceCode`表 - device_fingerprint字段存储设备唯一标识
- **激活尝试**: `DeviceCode`表 - activation_attempts字段记录尝试次数，最大3次
- **即时激活**: 用户输入激活码自助绑定设备，一设备一有效码
- **设备移除**: 通过is_deleted软删除

### 🧾 税务设置页
- **税务规则**: `TaxRule`表 - 税率和适用规则管理
- **州税/市税**: `TaxRule`表 - tax_type字段区分
- **生效时间**: `TaxRule`表 - effective_from、effective_until字段

### 🔔 通知/消息中心页
- **系统公告**: `Notification`表 - type字段标识公告类型
- **设备异常提醒**: `Notification`表 - 关联Device表异常状态
- **消息状态**: `Notification`表 - is_read、read_at字段
- **消息详情**: `Notification`表 - title、message字段

### 🔒 权限系统实现
- **角色定义**: `Role`表 - role_name、role_code
- **权限定义**: `Permission`表 - resource、action精确控制
- **用户角色**: `UserRole`表 - 多对多关系实现
- **角色权限**: `RolePermission`表 - 灵活权限组合
- **权限验证**: 通过用户→角色→权限的链式查询

### 🛡️ 数据安全与审计
- **操作审计**: 所有表的created_by、updated_by字段
- **数据保护**: 所有表的is_deleted软删除
- **幂等性**: Order、Payment表的idempotency_key防重复
- **数据追踪**: 统一的时间戳字段created_at、updated_at

## iPad POS API 数据库支持分析

基于iPad POS App的API需求，以下是当前数据库设计的支持情况和改进建议：

### ✅ **完全支持的API功能**

#### 3. 商品/菜单
- **GET /products**: `Product`表完整支持
- **GET /categories**: `Category`表完整支持  
- **离线缓存**: `updated_at`字段支持增量同步
- **库存管理**: `Inventory`表支持库存查询

#### 4. 购物车/订单
- **POST /orders**: `Order`表有`idempotency_key`防重复
- **PUT /orders/{id}**: `OrderItem`表支持订单更新
- **GET /orders/{id}**: 完整的订单查询支持
- **离线同步**: 幂等性设计支持批量上传

#### 5. 支付
- **POST /payments**: `Payment`表有`idempotency_key`
- **GET /payments/{id}**: 支付状态查询支持
- **POST /refunds**: 通过Payment状态管理退款

#### 7. 历史订单
- **GET /orders?date=...**: `Order`表`created_at`索引支持

#### 8. 日结/交班 (部分支持)
- **GET /reports/daily-sales**: `DailySalesReport`表支持
- **POST /closing**: ✅ 新增`Closing`表支持

#### 9. 设置/硬件
- **GET /settings**: `Store`表支持门店配置
- **POST /devices/register**: `Device`表完整支持

### 🟡 **需要字段扩展的功能**

#### 1. 登录/用户
- **POST /auth/login**: ✅ 需要在`User`表添加`pin_hash`字段
- **GET /auth/me**: ✅ 新增`UserSession`表支持多设备登录
- **离线登录**: UserSession表支持本地缓存

#### 6. 收据/打印
- **POST /receipts**: ✅ 新增`Receipt`表记录收据发送

### 🆕 **需要新增的表**

#### 2. 打卡/考勤
- **POST /attendance/clock-in**: ✅ 新增`Attendance`表
- **POST /attendance/clock-out**: Attendance表支持
- **离线打卡**: `sync_status`字段支持离线同步

### 📋 **新增表设计要点**

#### Attendance (考勤表)
- **idempotency_key**: 防止重复打卡上传
- **sync_status**: 支持离线打卡同步
- **clock_in_time/clock_out_time**: 精确记录打卡时间

#### UserSession (用户会话表)  
- **token_hash**: 支持多设备登录
- **device_id**: 关联具体设备
- **expires_at**: 会话过期管理

#### Closing (交班记录表)
- **cash_counted/cash_expected**: 现金盘点
- **difference**: 差额记录
- **sync_status**: 支持离线交班

#### Receipt (收据记录表)
- **delivery_method**: print/sms/email
- **recipient**: 收据接收方
- **status**: 发送状态跟踪

### 🔒 **关键设计原则**

1. **幂等性优先**: 所有关键操作表都有`idempotency_key`
2. **离线同步**: 新增`sync_status`字段管理同步状态  
3. **数据完整性**: 保持现有审计字段标准
4. **向后兼容**: 新增表不影响现有Dashboard功能

## 实体关系说明

### 核心实体

#### 1. 租户与用户管理模块
- **Store (店铺)**: 多租户架构的核心，所有业务数据都以店铺为隔离单位
- **User (用户/员工)**: 系统用户，包括管理员、员工等，通过store_id实现租户隔离
- **Role (角色)**: 权限角色定义，支持灵活的权限管理，使用role_code便于程序判断
- **Permission (权限)**: 细粒度权限定义，通过resource和action实现精确权限控制
- **UserRole (用户角色关联)**: 多对多关系，支持一个用户拥有多个角色
- **RolePermission (角色权限关联)**: 多对多关系，实现角色与权限的灵活组合

#### 2. 商品与库存管理模块
- **Category (商品分类)**: 商品分类管理，支持层级结构
- **Product (商品)**: 商品主表，包含基本信息和价格
- **Inventory (库存)**: 商品库存管理，与商品一对一关系，独立管理库存数据

#### 3. 订单与支付管理模块
- **Customer (客户)**: 客户信息管理，支持会员积分
- **Order (订单)**: 订单主表，记录订单基本信息和状态
- **OrderItem (订单项)**: 订单明细，记录每个商品的购买信息
- **Payment (支付)**: 支付记录，支持多种支付方式和分期支付
- **Coupon (优惠券)**: 优惠券管理
- **OrderCoupon (订单优惠券关联)**: 记录订单使用的优惠券

#### 4. 系统管理与配置模块
- **Device (设备)**: POS终端设备管理
- **DeviceCode (激活码)**: Square风格设备激活码管理，支持6-8位短码即时激活
- **TaxRule (税务规则)**: 税务配置管理，支持复杂税务场景
- **Notification (通知)**: 系统通知消息管理

#### 5. 报表与汇总模块
- **DailySalesReport (日销售汇总)**: 反范式化设计，提升报表查询性能

### 关键关系

#### 一对多关系 (1:N)
- Store → User: 一个店铺有多个员工
- Store → Product: 一个店铺有多个商品
- Store → Customer: 一个店铺有多个客户
- Category → Product: 一个分类包含多个商品
- Customer → Order: 一个客户可以有多个订单
- Order → OrderItem: 一个订单包含多个商品项
- Order → Payment: 一个订单可能有多次支付记录

#### 一对一关系 (1:1)
- Product ↔ Inventory: 商品与库存一对一关系

#### 多对多关系 (N:M)
- User ↔ Role: 通过UserRole表实现，用户可以有多个角色
- Role ↔ Permission: 通过RolePermission表实现，角色可以有多个权限
- Order ↔ Coupon: 通过OrderCoupon表实现，订单可以使用多张优惠券

### 设计特点

#### 1. 多租户架构
- 所有业务表都包含store_id字段，实现数据隔离
- 通过应用层和数据库约束确保租户间数据安全

#### 2. 数据完整性保障
- 使用自增主键确保唯一性
- 合理设置外键约束保证引用完整性
- 财务相关字段使用DECIMAL类型避免精度问题

#### 3. 性能优化考虑
- 创建DailySalesReport汇总表，避免复杂的实时统计查询
- 为高频查询字段设计复合索引
- 考虑订单表按时间分区提升查询效率

#### 4. 扩展性设计
- 预留状态字段支持业务流程扩展
- 使用JSON字段存储灵活配置信息
- 软删除设计保护重要历史数据

#### 5. 审计追踪与数据安全
- **统一审计字段**: 所有表包含created_at、updated_at、created_by、updated_by时间戳和操作人员信息
- **软删除设计**: 使用is_deleted字段保护重要历史数据，避免物理删除
- **幂等性保障**: Order和Payment表包含idempotency_key字段，防止重复提交和回调处理
- **数据变更追踪**: 支持完整的数据变更历史记录

#### 6. 权限系统设计
- **细粒度权限控制**: Permission表通过resource和action字段实现精确的权限定义
- **灵活的角色管理**: Role-Permission多对多关系，支持权限的灵活组合
- **权限继承**: 用户通过UserRole关联获得角色权限，支持多角色并存
- **权限审计**: 权限分配和撤销都有完整的操作记录

#### 7. 事务安全设计
- **支付幂等性**: Payment表的idempotency_key确保支付操作的幂等性
- **订单一致性**: Order表的idempotency_key防止重复下单
- **状态管理**: 订单和支付状态的严格控制，确保业务流程的正确性
- **回滚支持**: 软删除设计支持业务操作的安全回滚
