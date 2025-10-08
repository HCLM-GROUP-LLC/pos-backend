package com.hclm.satoken;

import cn.dev33.satoken.stp.StpUtil;

public class SaTokenUtil {

    /**
     * 登录
     *
     */
    public static void login(String id) {
        StpUtil.login(id);
    }

    /**
     * 获取登录id
     *
     * @return {@link String }
     */
    public static String getLoginId() {
        return StpUtil.getLoginId().toString();
    }

    /**
     * 获取令牌名称
     *
     * @return {@link String }
     */
    public static String getTokenName() {
        return StpUtil.getTokenName();
    }

    /**
     * 获取令牌值
     *
     * @return {@link String }
     */
    public static String getTokenValue() {
        return StpUtil.getTokenValue();
    }
}
