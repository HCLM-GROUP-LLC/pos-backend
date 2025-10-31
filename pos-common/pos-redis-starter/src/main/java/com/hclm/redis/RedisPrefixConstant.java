package com.hclm.redis;

/**
 * redis前缀常量
 *
 * @author hanhua
 * @since 2025/10/13
 */
public interface RedisPrefixConstant {
    /**
     * 设备代码
     */
    String DEVICE_CODE = "devicecode:";
    /**
     * 门店设备编号
     */
    String STORE_DEVICE_NUMBER = "store:device:number:";
    /**
     * 上次短信代码的时间 一分钟有效期
     */
    String LAST_SMS_CODE = "sms:code:last:";
    /**
     * 短信代码 5分钟有效期
     */
    String SMS_CODE = "sms:code:";
}
