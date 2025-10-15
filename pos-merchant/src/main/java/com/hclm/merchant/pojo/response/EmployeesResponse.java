package com.hclm.merchant.pojo.response;

import com.hclm.web.enums.EmployeesRoleEnum;
import com.hclm.web.enums.EmployeesSatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmployeesResponse {
    @Schema(description = "员工id")
    private String employeesId;
    @Schema(description = "商户id")
    private String merchantId;

    @Schema(description = "员工名")
    private String firstName;

    @Schema(description = "员工姓")
    private String lastName;

    /**
     * 电话号码
     */
    @Schema(description = "员工电话")
    private String phoneNumber;

    @Schema(description = "员工邮箱")
    private String email;

    @Schema(description = "门店id")
    private String storeId;

    @Schema(description = "员工角色")
    private EmployeesRoleEnum roleId;

    @Schema(description = "员工状态")
    private EmployeesSatusEnum status;
    @Schema(description = "员工最后登录时间")
    private Long lastLoginAt;
    @Schema(description = "员工创建时间")
    private Long createdAt;
    @Schema(description = "员工更新时间")
    private Long updatedAt;
}
