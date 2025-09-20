package com.example.pos_backend.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private UUID categoryId;
    private UUID storeId;
    private String categoryName;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
    private Boolean isDeleted;
}