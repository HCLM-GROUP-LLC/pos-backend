package com.hclm.web.constant;

/**
 * 表名常量
 *
 * @author hanhua
 * @since 2025/10/08
 */
public interface TableNameConstant {
    /**
     * 设备
     */
    String DEVICES = "devices";
    /**
     * 员工
     */
    String EMPLOYEES = "employees";
    /**
     * 员工考勤
     */
    String EMPLOYEES_ATTENDANCE = "attendance";
    /**
     * 平面图
     */
    String FLOOR_PLANS = "floor_plans";
    /**
     * 餐桌,tables是mysql的关键字，所以用dining_tables代替
     */
    String TABLES = "dining_tables";
}
