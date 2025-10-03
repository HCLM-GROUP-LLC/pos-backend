package com.example.pos_backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author hanhua
 * @since 2025/10/03
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum {
    /**
     * 商家
     */
    MERCHANT("merchant"),
    /**
     * 员工
     */
    EMPLOYEE("employee");
    private final String code;
    public static UserTypeEnum getByCode(String code) {
        for (UserTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
