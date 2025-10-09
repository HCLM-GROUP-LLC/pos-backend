package com.hclm.terminal.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmployeesLoginRequest {
    @NotEmpty(message = "{deviceId.notNull}")
    @Schema(description = "设备ID")
    private String deviceId;
    @NotEmpty(message = "{passCode.notNull}")
    private String passCode;
}
