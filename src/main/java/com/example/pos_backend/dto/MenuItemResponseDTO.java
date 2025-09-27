package com.example.pos_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuItemResponseDTO {
    private UUID id;
    private UUID orgId;
    private UUID storeId;
    private UUID categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
}
