package com.hclm.redis;

import com.hclm.redis.cache.DeviceCodeCache;

import java.time.Duration;

/**
 * 设备redis工具
 *
 * @author hanhua
 * @since 2025/10/07
 */
public class DeviceRedisUtil {
    private static final String PREFIX = "devicecode:";

    /**
     * 存在
     *
     * @param code 代码
     * @return boolean
     */
    public static boolean exists(String code) {
        return RedisUtil.exists(PREFIX + code);
    }

    /**
     * 获取
     *
     * @param code 代码
     * @return {@link DeviceCodeCache}
     */
    public static DeviceCodeCache get(String code) {
        return RedisUtil.get(PREFIX + code, DeviceCodeCache.class);
    }

    /**
     * 设置缓存
     *
     * @param code 代码
     */
    public static void set(String code, DeviceCodeCache cache) {
        RedisUtil.set(PREFIX + code, cache, Duration.ofMinutes(5));// 有效期5分钟
    }
}
