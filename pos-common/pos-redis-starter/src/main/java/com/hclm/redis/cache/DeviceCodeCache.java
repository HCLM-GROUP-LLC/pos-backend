package com.hclm.redis.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceCodeCache {
    /**
     * 门店id
     */
    private String storeId;
}
