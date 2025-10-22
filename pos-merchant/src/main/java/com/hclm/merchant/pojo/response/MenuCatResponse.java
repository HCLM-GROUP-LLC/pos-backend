package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MenuCatResponse {
    /**
     * 类别id
     */
    @Schema(description = "类别id")
    private Long categoryId;
    /**
     * 所属菜单id
     */
    @Schema(description = "所属菜单id")
    private Long menuId;
    /**
     * 所属门店id
     */
    @Schema(description = "所属门店id")
    private String storeId;
    /**
     * 类别名称
     */
    @Schema(description = "类别名称")
    private String categoryName;
}
