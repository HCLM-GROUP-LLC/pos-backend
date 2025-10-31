package com.hclm.redis;

import com.hclm.redis.cache.MerchantCodeCache;

import java.time.Duration;

/**
 * 商户 短信验证码 redis 缓存工具
 *
 * @author hanhua
 * @since 2025/10/31
 */
public class MrcSmsUtil {
    /**
     * 记录上次发送时间 一分钟有效
     *
     * @param phoneNumber 电话号码
     */
    public static void lastSendTime(String phoneNumber) {
        RedisUtil.set(RedisPrefixConstant.LAST_SMS_CODE + phoneNumber, System.currentTimeMillis(), Duration.ofMinutes(1));
    }

    /**
     * 获取上次发送时间
     *
     * @param phoneNumber 电话号码
     * @return 时间戳
     */
    public static Long getLastSendTime(String phoneNumber) {
        return RedisUtil.get(RedisPrefixConstant.LAST_SMS_CODE + phoneNumber, Long.class);
    }

    /**
     * 存在未失效的手机号
     *
     * @param phoneNumber 电话号码
     * @return boolean
     */
    public static boolean existsPhoneNumber(String phoneNumber) {
        return RedisUtil.exists(RedisPrefixConstant.LAST_SMS_CODE + phoneNumber);
    }

    /**
     * 短信代码 5分钟有效
     *
     * @param smsCode 短信代码
     * @param cache   缓存信息
     */
    public static void smsCode(String smsCode, MerchantCodeCache cache) {
        RedisUtil.set(RedisPrefixConstant.SMS_CODE + smsCode, cache, Duration.ofMinutes(5));
    }

    /**
     * 获取短信代码
     *
     * @param smsCode 短信代码
     * @return {@link String }
     */
    public static MerchantCodeCache getSmsCode(String smsCode) {
        return RedisUtil.get(RedisPrefixConstant.SMS_CODE + smsCode, MerchantCodeCache.class);
    }
}
