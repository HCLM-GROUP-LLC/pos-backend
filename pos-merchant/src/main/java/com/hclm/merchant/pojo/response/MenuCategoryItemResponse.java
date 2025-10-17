package com.hclm.merchant.pojo.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuCategoryItemResponse {
    private String id;
    private String menuId;
    private String categoryId;
    private String itemId;
    private Integer sortOrder;
}