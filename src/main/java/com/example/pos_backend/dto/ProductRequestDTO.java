package com.example.pos_backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotNull(message = "商户ID不能为空")
    private UUID merchantId;

    @NotNull(message = "门店ID不能为空")
    private UUID storeId;

    @NotNull(message = "分类ID不能为空")
    private UUID categoryId;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称不能超过100个字符")
    private String productName;

    private String description;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    private BigDecimal price;

    private String imageUrl;

    @Builder.Default
    private Boolean isActive = true;

    @NotNull(message = "创建人不能为空")
    private UUID createdBy;
}

