package com.example.pos_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequestDTO {
    @NotNull(message = "商品ID不能为空")
    private UUID productId;
    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    private Double costPrice;
    @NotNull(message = "创建人不能为空")
    private UUID createdBy;
}
