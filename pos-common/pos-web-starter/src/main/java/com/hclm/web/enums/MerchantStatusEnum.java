package com.hclm.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MerchantStatusEnum {
    /**
     * 禁用
     */
    DISABLED("DISABLED"),
    /**
     * 启用
     */
    ENABLED("ACTIVE");
    private final String code;
}
