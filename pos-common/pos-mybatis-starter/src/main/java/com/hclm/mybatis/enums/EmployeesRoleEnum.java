package com.hclm.mybatis.enums;


/**
 * 员工角色枚举
 * 这里的枚举值与数据库中的角色值对应，不要修改
 * {@link cn.dev33.satoken.annotation.SaCheckRole} 注解也会用到
 *
 * @author hanhua
 * @since 2025/10/09
 */
public enum EmployeesRoleEnum {
    /**
     * 收银员
     */
    Cashier,
    /**
     * 服务员
     */
    Server,
    /**
     * 店长
     */
    Manager,
    /**
     * 管理员
     */
    Admin
}
