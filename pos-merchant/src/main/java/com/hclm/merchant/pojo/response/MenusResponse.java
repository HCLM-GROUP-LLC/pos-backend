package com.hclm.merchant.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MenusResponse {
    @Schema(description = "菜单id")
    private Long menuId;
    @Schema(description = "所属门店id")
    private String storeId;
    @Schema(description = "菜单名称")
    private String menuName;
}
