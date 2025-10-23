package com.hclm.terminal.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MenusResponse {
    @Schema(description = "菜单id")
    private Long menuId;
    @Schema(description = "菜单名称")
    private String menuName;
    @Schema(description = "分类")
    private List<MenuCatResponse> cats;
}
