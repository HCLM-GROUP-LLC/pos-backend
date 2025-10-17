package com.hclm.merchant.pojo.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuCategoryRequest {
    private String name;        // 类目名称（如主菜、饮料）
    private String description; // 类目描述
    private Integer sortOrder;  // 排序顺序
}
