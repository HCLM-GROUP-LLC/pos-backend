package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备id生成请求dto
 *
 * @author hanhua
 * @since 2025/10/04
 */
@Data
public class DeviceIDGenRequest {
    /**
     * 商店id
     */
    @Schema(description = "商店id")
    @NotNull(message = "{storeId.notNull}")
    private String storeId;
    /**
     * 数量
     */
    @Schema(description = "数量")
    @Min(value = 1, message = "{quantity.min}")
    @NotNull(message = "{quantity.notNull}")
    private Integer quantity;
}
