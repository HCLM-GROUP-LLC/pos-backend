package com.hclm.terminal.pojo.cache;

import com.hclm.web.enums.EmployeesRoleEnum;
import lombok.Data;

/**
 * 员工登录缓存
 * 主要是一些id、角色等信息
 *
 * @author hanhua
 * @since 2025/10/09
 */
@Data
public class EmployeesLoginCache {
    /**
     * 员工id
     */
    private String employeesId;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 商店id
     */
    private String storeId;
    /**
     * 角色
     */
    private EmployeesRoleEnum role;
    /**
     * 上次登录于
     */
    private Long lastLoginAt;
    /**
     * 出勤id
     */
    private Long attendanceId;
}
