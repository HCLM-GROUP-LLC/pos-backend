package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuCatAddRequest {
    /**
     * 菜单id
     */
    @NotNull
    @Schema(description = "所属菜单id")
    private Long menuId;
    /**
     * 类别名称
     */
    @NotEmpty
    @Schema(description = "类别名称")
    private String categoryName;
}
