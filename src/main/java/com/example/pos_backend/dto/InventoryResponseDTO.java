package com.example.pos_backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDTO {
    private UUID inventoryId;
    private UUID productId;
    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    private BigDecimal costPrice;
    private Boolean isDeleted;
}
