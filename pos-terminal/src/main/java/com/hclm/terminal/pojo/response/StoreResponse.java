package com.hclm.terminal.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

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
    private Instant createdAt;
    private Instant updatedAt;
}
