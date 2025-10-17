package com.hclm.merchant.pojo.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemResponse {
    private String itemId;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String status;
}
