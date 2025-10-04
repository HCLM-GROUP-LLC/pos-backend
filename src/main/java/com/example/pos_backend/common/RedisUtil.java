package com.example.pos_backend.common;

import com.alibaba.fastjson2.support.redission.JSONCodec;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.codec.StringCodec;

public class RedisUtil {
    private static RedissonClient client;

    /**
     * redis工具
     *
     * @param redissonClient redisson客户端
     */
    public RedisUtil(RedissonClient redissonClient) {
        client = redissonClient;
    }

    /**
     * 获取编解码器
     *
     * @param valueClass 类型
     * @return 基本编解码器
     */
    private static BaseCodec getCodec(Class<?> valueClass) {
        if (String.class.isAssignableFrom(valueClass)) {
            return StringCodec.INSTANCE;
        }
        return new JSONCodec(valueClass);
    }

    /**
     * 获取桶
     *
     * @param key        键
     * @param valueClass 类型
     * @param <T>        数据类型
     * @return 桶
     */
    public static <T> RBucket<T> getBucket(String key, Class<T> valueClass) {
        return client.getBucket(key, getCodec(valueClass));
    }

    /**
     * 获取Set结构的Redis集合
     *
     * @param key        键
     * @param valueClass 元素类型
     * @param <T>        数据类型
     * @return RSet实例
     */
    public static <T> RSet<T> getSet(String key, Class<T> valueClass) {
        return client.getSet(key, getCodec(valueClass));
    }

    /**
     * 是否存在
     *
     * @param key 钥匙
     * @return boolean
     */
    public static boolean exists(String key) {
        return client.getBucket(key).isExists();
    }
}
