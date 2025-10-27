package com.hclm.terminal.utils;

import org.springframework.core.NamedThreadLocal;

/**
 * 门店上下文
 *
 * @author hanhua
 * @since 2025/10/27
 */
public class StoreContext {
    /**
     * 设备id
     */
    private static final ThreadLocal<String> DEVICE_ID = new NamedThreadLocal<>("DeviceId");
    private static final ThreadLocal<String> STORE_ID = new NamedThreadLocal<>("StoreId");


    /**
     * 获取设备id
     *
     * @return 设备id
     */
    public static String getDeviceId() {
        return DEVICE_ID.get();
    }

    /**
     * 设置设备id
     *
     * @param deviceId 设备id
     */
    public static void setDeviceId(String deviceId) {
        DEVICE_ID.set(deviceId);
    }

    /**
     * 获取门店id
     *
     * @return 门店id
     */
    public static String getStoreId() {
        return STORE_ID.get();
    }

    /**
     * 设置门店id
     *
     * @param storeId 门店id
     */
    public static void setStoreId(String storeId) {
        STORE_ID.set(storeId);
    }
}
