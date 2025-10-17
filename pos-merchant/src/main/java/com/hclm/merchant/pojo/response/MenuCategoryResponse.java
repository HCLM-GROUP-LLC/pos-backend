package com.hclm.merchant.pojo.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuCategoryResponse {
    private String categoryId;
    private String name;
    private String description;
    private Integer sortOrder;
}

