package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MenusAddRequest {
    /**
     * 所属门店id
     */
    @NotEmpty
    @Schema(description = "所属门店id")
    private String storeId;
    @NotEmpty
    @Schema(description = "菜单名称")
    private String menuName;
}
