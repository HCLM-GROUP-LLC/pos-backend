package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuItemResponse {
    /**
     * 单品id
     */
    @Schema(description = "单品id")
    private Long itemId;
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
    /**
     * 菜品简介
     */
    @Schema(description = "菜品简介")
    private String itemDescription;
    /**
     * 单价
     */
    @Schema(description = "单价")
    private BigDecimal itemPrice;
    /**
     * 注释标记
     */
    @Schema(description = "词条")
    private List<String> noteTags;
}
