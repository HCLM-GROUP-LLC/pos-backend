package com.hclm.terminal.pojo.cache;

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
    private String employeesId;
    private String merchantId;
    private String storeId;
    private String roleId;
    private Long lastLoginAt;
}
