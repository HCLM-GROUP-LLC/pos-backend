package com.example.pos_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MenuItemRequestDTO {
    @NotNull(message = "Organization ID is required")
    private UUID orgId;
    
    @NotNull(message = "Store ID is required")
    private UUID storeId;
    
    @NotNull(message = "Category ID is required")
    private UUID categoryId;
    
    @NotBlank(message = "Item name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    private String currency;
    private String imageUrl;
    private Integer sortOrder;
    
    private UUID createdBy;
    private UUID updatedBy;
}
