package com.example.pos_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuCategoryResponseDTO {
    private UUID id;
    private UUID orgId;
    private UUID storeId;
    private String name;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
}
