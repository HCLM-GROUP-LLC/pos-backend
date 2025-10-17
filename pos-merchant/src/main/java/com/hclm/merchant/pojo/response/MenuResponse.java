package com.hclm.merchant.pojo.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuResponse {
    private String menuId;
    private String merchantId;
    private String name;
    private String description;
    private Long activeFrom;
    private Long activeTo;
    private String status;
    private Long createdAt;
    private Long updatedAt;
}

