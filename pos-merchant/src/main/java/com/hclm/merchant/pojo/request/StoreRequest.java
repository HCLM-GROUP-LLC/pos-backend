package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "店铺创建或更新请求参数")
public class StoreRequest {

    @Schema(description = "所属商户ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull
    private String merchantId;

    @Schema(description = "店铺名称", example = "Sunny Café")
    @NotNull
    private String storeName;

    @Schema(description = "地址", example = "123 Orchard Road")
    private String address;

    @Schema(description = "时区", example = "Asia/Singapore")
    private String timezone;

    @Schema(description = "状态", example = "ACTIVE")
    private String status;

    @Schema(description = "税率", example = "0.0700")
    private BigDecimal taxRate;

    @Schema(description = "货币单位", example = "SGD")
    private String currency;

    @Schema(description = "营业时间(JSON)", example = "{\"mon\":\"09:00-21:00\"}")
    private String businessHours;
}
