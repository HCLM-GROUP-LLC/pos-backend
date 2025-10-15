package com.hclm.web.enums;

/**
 * 餐桌状态枚举
 *
 * @author hanhua
 * @since 2025/10/13
 */
public enum DiningTableStatusEnum {
    /**
     * 空闲
     */
    AVAILABLE,
    /**
     * 使用中
     */
    OCCUPIED,
    /**
     * 预定
     */
    RESERVED,
    /**
     * 结账中
     */
    CHECKOUTING,
    /**
     * 禁用
     */
    DISABLED
}
