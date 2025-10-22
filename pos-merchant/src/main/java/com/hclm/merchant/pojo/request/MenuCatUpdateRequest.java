package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 菜单类别更新要求
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Data
public class MenuCatUpdateRequest {
    @NotEmpty
    @Schema(description = "类别名称")
    private String categoryName;
}
