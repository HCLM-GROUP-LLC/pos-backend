
---

# 🍱 菜单与类目模块（Menu & Item Management）

```
Menu (菜单)                            MenuCategory (类目)
┌──────────────────────────┐           ┌──────────────────────────┐
│menu_id (UUID, PK)        │───1:N────│category_id (UUID, PK)     │
│merchant_id (FK, UUID)     │          │merchant_id (FK, UUID)     │
│name                      │          │name                       │
│description               │          │description                │
│start_time                │          │display_order              │
│end_time                  │          │is_active (TINYINT)        │
│is_active (TINYINT)       │          │created_at                 │
│created_at                │          │updated_at                 │
│updated_at                │          │is_deleted (TINYINT)       │
│is_deleted (TINYINT)      │          └──────────────────────────┘
└──────────────────────────┘

Item (菜品 / 套餐)
┌──────────────────────────┐
│item_id (UUID, PK)        │
│merchant_id (FK, UUID)    │
│door_port_id (FK, BIGINT) │→ 对应打印档口
│name                      │
│description               │
│price (DECIMAL 10,2)      │
│image_url                 │
│is_combo (TINYINT)        │
│combo_items (JSON)        │
│status (VARCHAR)          │
│is_active (TINYINT)       │
│created_at                │
│updated_at                │
│is_deleted (TINYINT)      │
└──────────────────────────┘

MenuCategoryItem (菜单-类目-菜品关联)
┌──────────────────────────┐
│id (AUTO_INCREMENT, PK)    │
│menu_id (FK, UUID)         │
│category_id (FK, UUID)     │
│item_id (FK, UUID)         │
│display_order              │
│created_at                 │
│updated_at                 │
└──────────────────────────┘
```
---

# 🏢 商家与门店模块（Merchant & Store Management）

```
Merchant (商家)
┌──────────────────────────┐
│merchant_id (UUID, PK)     │
│email (UNIQUE)             │
│phone_number               │
│password_hash              │
│name                       │
│business_name              │
│business_address           │
│status (ACTIVE/INACTIVE)   │
│last_login_at (BIGINT)     │
│created_at                 │
│updated_at                 │
│is_deleted (TINYINT)       │
└──────────────────────────┘

Store (门店)
┌──────────────────────────┐
│id (UUID, PK)              │
│merchant_id (FK, UUID)     │
│store_name                 │
│address                    │
│timezone                   │
│status                     │
│tax_rate (DECIMAL)         │
│currency (VARCHAR 3)       │
│business_hours (JSON)      │
│created_at                 │
│updated_at                 │
│is_deleted (TINYINT)       │
└──────────────────────────┘

MerchantBankCard (商家银行卡)
┌──────────────────────────┐
│id (BIGINT, PK)            │
│merchant_id (FK, UUID)     │
│bank_card_no               │
│bank_name                  │
│created_at                 │
│updated_at                 │
└──────────────────────────┘
```

---

# 👨‍💼 员工与考勤模块（Employee & Attendance）

```
Employee (员工)
┌──────────────────────────┐
│employees_id (UUID, PK)    │
│merchant_id (FK, UUID)     │
│store_id (FK, UUID)        │
│email                      │
│phone_number               │
│pass_code (PIN哈希)        │
│first_name                 │
│last_name                  │
│role_id (FK)               │
│status (ACTIVE/INACTIVE)   │
│last_login_at              │
│created_at                 │
│updated_at                 │
│is_deleted (TINYINT)       │
└──────────────────────────┘

Attendance (考勤)
┌──────────────────────────┐
│attendance_id (PK, BIGINT) │
│merchant_id (FK, UUID)     │
│employees_id (FK, UUID)    │
│store_id (FK, UUID)        │
│clock_in_time              │
│clock_out_time             │
│total_time                 │
└──────────────────────────┘
```

---

# 🧾 订单模块（Order Management）

```
Order (订单)
┌──────────────────────────┐
│id (BIGINT, PK)            │
│merchant_id (FK, UUID)     │
│order_no (VARCHAR)         │
│order_type (堂食/外带)      │
│order_status (VARCHAR)     │
│total_amount (DECIMAL)     │
│created_at                 │
│updated_at                 │
└──────────────────────────┘
```

---

# 🖨️ 打印与档口模块（Printer & DoorPort）

```
PrinterDoorPort (打印机档口)
┌──────────────────────────┐
│id (BIGINT, PK)            │
│merchant_id (FK, UUID)     │
│device_id (FK, UUID)       │→ 对应 Devices
│door_port_name             │
│created_at                 │
│updated_at                 │
└──────────────────────────┘

DoorPortPrinter (档口打印机关联)
┌──────────────────────────┐
│id (BIGINT, PK)            │
│merchant_id (FK, UUID)     │
│door_port_id (FK, BIGINT)  │
│device_id (FK, UUID)       │
│created_at                 │
│updated_at                 │
└──────────────────────────┘

PrintTemplate (打印模板)
┌──────────────────────────┐
│id (BIGINT, PK)            │
│merchant_id (FK, UUID)     │
│template_name              │
│template_content (TEXT)    │
│created_at                 │
│updated_at                 │
└──────────────────────────┘
```

---

# 🪑 楼层与桌台模块（Layout / Floor Plan）

```
FloorPlan (楼层平面图)
┌──────────────────────────┐
│floor_plan_id (BIGINT, PK) │
│store_id (FK, UUID)        │
│floor_plan_name            │
│width, height              │
│tables_number              │
│capacity                   │
└──────────────────────────┘

DiningTable (桌台)
┌──────────────────────────┐
│dining_table_id (BIGINT, PK)│
│merchant_id (FK, UUID)      │
│store_id (FK, UUID)         │
│floor_plan_id (FK, BIGINT)  │
│dining_table_name           │
│capacity                    │
│used_capacity               │
│shape, width, height        │
│position_x, position_y      │
│status (占用/空闲)          │
│opener (FK 员工ID)         │
└──────────────────────────┘
```

---

# 💡 系统设备模块（Device Management）

```
Device (设备)
┌──────────────────────────┐
│device_id (UUID, PK)       │
│merchant_id (FK, UUID)     │
│store_id (FK, UUID)        │
│device_name                │
│device_type                │
│mac_address                │
│ip_address                 │
│last_online (BIGINT)       │
│status (ONLINE/OFFLINE)    │
│registered_at              │
│last_login_at              │
│is_deleted (TINYINT)       │
└──────────────────────────┘
```

---

