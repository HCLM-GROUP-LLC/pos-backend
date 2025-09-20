package com.example.pos_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    @NotNull(message = "门店ID不能为空")
    private UUID storeId;

    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    private String description;
    private Integer displayOrder;
    private Boolean isActive = true;
    @NotNull(message = "创建人不能为空")
    private UUID createdBy;
}