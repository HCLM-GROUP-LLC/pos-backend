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
}
