package com.example.pos_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MenuCategoryRequestDTO {
    @NotNull(message = "Organization ID is required")
    private UUID orgId;
    
    @NotNull(message = "Store ID is required")
    private UUID storeId;
    
    @NotBlank(message = "Category name is required")
    private String name;
    
    private Integer sortOrder;
    
    private UUID createdBy;
    private UUID updatedBy;
}
