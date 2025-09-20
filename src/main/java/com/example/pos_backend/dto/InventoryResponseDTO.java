package com.example.pos_backend.dto;

import lombok.*;
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
    private Double costPrice;
    private Boolean isDeleted;
}
