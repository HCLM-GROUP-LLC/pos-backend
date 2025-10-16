package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EmployeesCopyRequest {
    
    @NotBlank
    @Schema(description = "源店铺ID")
    private String sourceStoreId;
    
    @NotBlank
    @Schema(description = "目标店铺ID")
    private String targetStoreId;
    
    @NotEmpty
    @Schema(description = "要复制的员工ID列表")
    private List<String> employeeIds;
    
    @Schema(description = "是否保留原有角色", defaultValue = "true")
    private Boolean keepOriginalRole = true;
    
    @Schema(description = "是否保留原有状态", defaultValue = "true")
    private Boolean keepOriginalStatus = true;
}