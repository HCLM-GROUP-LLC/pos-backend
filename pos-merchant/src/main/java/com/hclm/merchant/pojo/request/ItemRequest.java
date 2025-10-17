package com.hclm.merchant.pojo.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequest {
    private String name;         // 菜品名称
    private String description;  // 菜品描述
    private Double price;        // 价格
    private String imageUrl;     // 图片URL
    private String status;       // 状态：AVAILABLE / UNAVAILABLE
}
