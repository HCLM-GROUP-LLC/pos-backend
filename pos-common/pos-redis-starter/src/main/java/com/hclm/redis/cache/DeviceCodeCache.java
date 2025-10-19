package com.hclm.redis.cache;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeviceCodeCache {
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 门店id
     */
    private String storeId;
}
