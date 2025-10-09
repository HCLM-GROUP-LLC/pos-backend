package com.hclm.satoken;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;

public class SaTokenUtil {
    private static final String DATA_KEY = "data";

    /**
     * 检查登录
     *
     */
    public static void checkLogin() {
        StpUtil.checkLogin();
    }

    /**
     * 登录
     *
     */
    public static void login(String id) {
        StpUtil.login(id);
    }

    /**
     * 登录
     *
     * @param id   id
     * @param data 数据
     */
    public static <T> void login(String id, T data) {
        StpUtil.login(id);
        StpUtil.getSession().set(DATA_KEY, data);
    }

    /**
     * 获取数据
     *
     * @return {@link T }
     */
    @SuppressWarnings("unchecked")
    public static <T> T getData(Class<T> clazz) {
        Object data = StpUtil.getSession().get(DATA_KEY);
        if (data instanceof JSONObject jsonObject) {
            return jsonObject.to(clazz);
        }
        if (data.getClass().isAssignableFrom(clazz)) {
            return (T) data;
        }
        return null;
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
