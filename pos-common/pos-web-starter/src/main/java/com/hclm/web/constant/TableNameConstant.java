package com.hclm.web.constant;

/**
 * 表名常量
 *
 * @author hanhua
 * @since 2025/10/08
 */
public interface TableNameConstant {
    /**
     * 商家
     */
    String MERCHANT = "merchants";
    /**
     * 门店
     */
    String STORE = "stores";
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
    /**
     * 菜单
     */
    String MENUS = "menus";
    /**
     * 菜单类别
     */
    String MENU_CATEGORYS = "menu_categorys";
    /**
     * 菜单项（菜品）
     */
    String MENU_ITEMS = "items";
}
