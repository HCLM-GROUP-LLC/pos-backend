package com.hclm.terminal.pojo.response;

import com.hclm.mybatis.enums.EmployeesRoleEnum;
import com.hclm.mybatis.enums.EmployeesSatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工登录响应
 *
 * @author hanhua
 * @since 2025/10/09
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeesLoginResponse {
    /**
     * 用户ID（UUID格式）
     */
    @Schema(description = "用户ID")
    private String employeesId;

    /**
     * 商家ID
     */
    @Schema(description = "商家ID")
    private String merchantId;

    /**
     * 门店ID
     */
    @Schema(description = "门店ID")
    private String storeId;

    /**
     * 邮箱（唯一）
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * PIN码哈希
     */
    @Schema(description = "PIN码哈希")
    private String passCode;

    /**
     * 名
     */
    @Schema(description = "名")
    private String firstName;

    /**
     * 姓
     */
    @Schema(description = "姓")
    private String lastName;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private EmployeesRoleEnum roleId;

    /**
     * 账号状态
     */
    @Schema(description = "账号状态")
    private EmployeesSatusEnum status;
    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    private Long lastLoginAt;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createdAt;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Long updatedAt;
    @Schema(description = "令牌名称")
    private String tokenName;
    @Schema(description = "令牌值")
    private String tokenValue;
}
