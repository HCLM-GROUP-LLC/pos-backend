package com.hclm.redis;

import com.alibaba.fastjson2.support.redission.JSONCodec;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.codec.StringCodec;

import java.time.Duration;

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
     * @param key 键
     * @return boolean
     */
    public static boolean exists(String key) {
        return client.getBucket(key).isExists();
    }

    /**
     * 获取缓存
     *
     * @param key        键
     * @param valueClass 值类
     * @return {@link T }
     */
    public static <T> T getCache(String key, Class<T> valueClass) {
        return getBucket(key, valueClass).get();
    }

    /**
     * 向Redis集合中添加元素
     *
     * @param key   键
     * @param value 价值
     */
    @SuppressWarnings("unchecked")
    public static <T> void addSet(String key, T value) {
        getSet(key, (Class<T>) value.getClass()).add(value);
    }

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    @SuppressWarnings("unchecked")
    public static <T> void set(String key, T value) {
        getBucket(key, (Class<T>) value.getClass()).set(value);
    }

    /**
     * 设置缓存
     *
     * @param key      键
     * @param value    值
     * @param duration 时长
     */
    @SuppressWarnings("unchecked")
    public static <T> void set(String key, T value, Duration duration) {
        getBucket(key, (Class<T>) value.getClass()).set(value, duration);
    }

    /**
     * 获取
     *
     * @param key        键
     * @param valueClass 值类
     * @return {@link T }
     */
    public static <T> T get(String key, Class<T> valueClass) {
        return getBucket(key, valueClass).get();
    }

    /**
     * 删除
     *
     * @param key 钥匙
     */
    public static void delete(String key) {
        client.getBucket(key).delete();
    }
}
