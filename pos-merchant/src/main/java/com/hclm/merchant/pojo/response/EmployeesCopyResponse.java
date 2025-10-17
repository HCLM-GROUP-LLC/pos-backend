package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeesCopyResponse {
    
    @Schema(description = "成功复制的员工数量")
    private Integer successCount;
    
    @Schema(description = "失败的员工数量")
    private Integer failedCount;
    
    @Schema(description = "成功复制的员工信息")
    private List<EmployeesResponse> successEmployees = new ArrayList<>();
    
    @Schema(description = "失败的员工ID及原因")
    private List<FailedEmployee> failedEmployees = new ArrayList<>();
    
    @Data
    public static class FailedEmployee {
        @Schema(description = "员工ID")
        private String employeeId;
        
        @Schema(description = "失败原因")
        private String reason;
    }
}