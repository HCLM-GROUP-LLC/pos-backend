package com.hclm.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备状态枚举
 *
 * @author hanhua
 * @since 2025/10/05
 */
@AllArgsConstructor
@Getter
public enum DeviceStatusEnum {
    /**
     * 在线
     */
    ONLINE("ONLINE"),
    /**
     * 离线
     */
    OFFLINE("OFFLINE");
    private final String code;
}
