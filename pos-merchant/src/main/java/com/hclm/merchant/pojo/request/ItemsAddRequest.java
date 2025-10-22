package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ItemsAddRequest {
    /**
     * 所属门店id
     */
    @Schema(description = "所属门店id")
    private String storeId;
    /**
     * 单品名称
     */
    @Schema(description = "单品名称")
    private String itemName;
}
