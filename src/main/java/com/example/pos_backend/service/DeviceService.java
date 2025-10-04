package com.example.pos_backend.service;

import com.example.pos_backend.common.RandomUtil;
import com.example.pos_backend.common.RedisUtil;
import com.example.pos_backend.dto.cache.DeviceCodeCache;
import com.example.pos_backend.dto.request.DeviceIDGenRequestDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DeviceService {
    /**
     * 生成代码
     *
     * @param request 请求
     * @return {@link List }<{@link String }>
     */
    public Set<String> genCodes(DeviceIDGenRequestDTO request) {
        int quantity = request.getQuantity();// 数量
        Set<String> codes = HashSet.newHashSet(quantity);
        int i = 0;
        while (codes.size() < quantity) {// 生成数量等于数量时，结束循环
            String code = RandomUtil.upperAndNumbers(12); // 生成设备授权码，12位，大写字母和数字
            if (!RedisUtil.exists("devicecode:" + code) && codes.add(code)) {// 判断授权码是否存在,避免重复生成
                RedisUtil.getBucket("devicecode:" + code, DeviceCodeCache.class)
                        .set(new DeviceCodeCache(request.getStoreId()), Duration.ofMinutes(5));// 设置授权码缓存，有效期5分钟
            }
        }
        return codes;
    }
}
