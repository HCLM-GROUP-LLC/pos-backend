package com.hclm.resource;

import lombok.Getter;

public class QuotaExceededException extends RuntimeException {
    /**
     * 商户id
     */
    @Getter
    private final String merchantId;

    public QuotaExceededException(String merchantId) {
        super("容量超出");
        this.merchantId = merchantId;
    }
}
