package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuItemUpdateRequest {
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

    @Schema(hidden = true)
    public boolean isEmpty() {
        return itemName == null && itemDescription == null && itemPrice == null && noteTags == null;
    }
}
