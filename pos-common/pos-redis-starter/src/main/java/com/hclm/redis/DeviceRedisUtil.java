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

    /**
     * 存在
     *
     * @param code 代码
     * @return boolean
     */
    public static boolean exists(String code) {
        return RedisUtil.exists(RedisPrefixConstant.DEVICE_CODE + code);
    }

    /**
     * 获取
     *
     * @param code 代码
     * @return {@link DeviceCodeCache}
     */
    public static DeviceCodeCache get(String code) {
        return RedisUtil.get(RedisPrefixConstant.DEVICE_CODE + code, DeviceCodeCache.class);
    }

    /**
     * 设置缓存
     *
     * @param code 代码
     */
    public static void set(String code, DeviceCodeCache cache) {
        RedisUtil.set(RedisPrefixConstant.DEVICE_CODE + code, cache, Duration.ofDays(1));// 一天的有效期
    }

    /**
     * 删除
     *
     * @param code 代码
     */
    public static void delete(String code) {
        RedisUtil.delete(RedisPrefixConstant.DEVICE_CODE + code);
    }

    /**
     * 获取下一个设备编号
     *
     * @param storeId 门店id
     * @return {@link Integer }
     */
    public static Long getDeviceNumberNext(String storeId) {
        return RedisUtil.increment(RedisPrefixConstant.STORE_DEVICE_NUMBER + storeId);
    }
}
