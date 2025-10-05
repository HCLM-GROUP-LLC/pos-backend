package com.example.pos_backend.dto.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备授权码缓存
 *
 * @author hanhua
 * @since 2025/10/04
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceCodeCache {
    /**
     * 门店id
     */
    private String storeId;
}
