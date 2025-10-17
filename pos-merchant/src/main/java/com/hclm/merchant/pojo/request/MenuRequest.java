package com.hclm.merchant.pojo.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuRequest {
    private String merchantId;      // 商家ID
    private String name;            // 菜单名称
    private String description;     // 菜单描述
    private Long activeFrom;        // 启用时间（时间戳）
    private Long activeTo;          // 结束时间（时间戳）
    private String status;          // 菜单状态：ACTIVE / INACTIVE
}
