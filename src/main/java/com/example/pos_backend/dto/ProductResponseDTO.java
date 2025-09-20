package com.example.pos_backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private UUID productId;
    private UUID merchantId;
    private UUID storeId;
    private UUID categoryId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
