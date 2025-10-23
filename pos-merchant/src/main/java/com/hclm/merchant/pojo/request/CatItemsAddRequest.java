package com.hclm.merchant.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CatItemsAddRequest {
    @NotEmpty
    @Schema(description = "菜品id")
    private List<Long> itemIds;
    @NotNull
    @Schema(description = "类别id")
    private Long categoryId;
}
