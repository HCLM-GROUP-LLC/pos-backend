package com.hclm.merchant.pojo.request;

import com.hclm.web.enums.EmployeesRoleEnum;
import com.hclm.web.enums.EmployeesSatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmployeesAddRequest {
    @NotEmpty
    @Schema(description = "员工名")
    private String firstName;

    @NotEmpty
    @Schema(description = "员工姓")
    private String lastName;

    /**
     * 电话号码
     */
    @NotEmpty
    @Pattern(regexp = "^\\+[1-9]\\d{0,3}[\\s\\-()]?[0-9]{3,14}$", message = "{phonenumber.forma}")
    @Schema(description = "员工电话")
    private String phoneNumber;

    @NotEmpty
    @Email
    @Schema(description = "员工邮箱")
    private String email;
    @NotEmpty
    @Schema(description = "门店id")
    private String storeId;

    @NotNull
    @Schema(description = "员工角色")
    private EmployeesRoleEnum roleId;

    @NotNull
    @Schema(description = "员工状态")
    private EmployeesSatusEnum status;
}
