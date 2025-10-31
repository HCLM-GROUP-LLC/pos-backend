package com.hclm.redis.cache;

import lombok.Data;

@Data
public class MerchantCodeCache {
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 商户id
     */
    private String merchantId;
}
