package com.example.pos_backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户端类型枚举
 *
 * @author hanhua
 * @since 2025/10/03
 */
@AllArgsConstructor
@Getter
public enum ClientTypeEnum {
    /**
     * web端，主要是商家端的管理后台
     */
    WEB("web"),
    /**
     * ios端，主要是员工进行使用
     */
    IOS("ios")
    ;
    private final String code;
}
