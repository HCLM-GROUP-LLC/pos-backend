package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "店铺信息返回对象")
public class StoreResponse {
    private String id;
    private String merchantId;
    private String storeName;
    private String address;
    private String timezone;
    private String status;
    private BigDecimal taxRate;
    private String currency;
    private String businessHours;
    private Long createdAt;
    private Long updatedAt;
}
