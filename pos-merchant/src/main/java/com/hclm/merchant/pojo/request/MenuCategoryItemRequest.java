package com.hclm.merchant.pojo.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuCategoryItemRequest {
    private String menuId;       // 菜单ID
    private String categoryId;   // 类目ID
    private String itemId;       // 菜品ID
    private Integer sortOrder;   // 排序
}

