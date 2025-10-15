package com.hclm.merchant.utils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 商户登录工具
 *
 * @author hanhua
 * @since 2025/10/14
 */
public class MerchantLoginUtil {
    /**
     * 登录
     *
     * @param merchantId 商户id
     */
    public static void login(String merchantId) {
        StpUtil.login(merchantId);
    }

    /**
     * 获取商户id
     *
     * @return {@link String }
     */
    public static String getMerchantId() {
        return StpUtil.getLoginIdAsString();
    }
}
