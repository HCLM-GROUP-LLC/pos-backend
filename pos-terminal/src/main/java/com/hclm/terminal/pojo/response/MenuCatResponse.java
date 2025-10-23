package com.hclm.terminal.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

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
     * 类别名称
     */
    @Schema(description = "类别名称")
    private String categoryName;
    /**
     * 菜单项
     */
    @Schema(description = "菜单项")
    private List<MenuItemResponse> items;
}
