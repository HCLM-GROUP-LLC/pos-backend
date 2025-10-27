package com.hclm.terminal.pojo.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmployeesLoginRequest {
    @NotEmpty(message = "{passCode.notNull}")
    private String passCode;
}
