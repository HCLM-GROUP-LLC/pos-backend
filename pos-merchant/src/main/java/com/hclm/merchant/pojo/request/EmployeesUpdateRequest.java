package com.hclm.merchant.pojo.request;

import com.hclm.mybatis.enums.EmployeesRoleEnum;
import com.hclm.mybatis.enums.EmployeesSatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeesUpdateRequest {

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

    @NotNull
    @Schema(description = "员工角色")
    private EmployeesRoleEnum roleId;

    @NotNull
    @Schema(description = "员工状态")
    private EmployeesSatusEnum status;
}
