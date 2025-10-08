package com.hclm.merchant.service;

import com.hclm.merchant.pojo.request.DeviceIDGenRequest;
import com.hclm.redis.DeviceRedisUtil;
import com.hclm.redis.cache.DeviceCodeCache;
import com.hclm.web.repository.DeviceRepository;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceManagerService {
    private final DeviceRepository deviceRepository;

    /**
     * 生成代码
     *
     * @param request 请求
     * @return {@link List }<{@link String }>
     */
    public Set<String> genCodes(DeviceIDGenRequest request) {
        int quantity = request.getQuantity();// 数量
        Set<String> codes = new HashSet<>(quantity);
        while (codes.size() < quantity) {// 生成数量等于数量时，结束循环
            String code = RandomUtil.upperAndNumbers(12); // 生成设备授权码，12位，大写字母和数字
            if (!DeviceRedisUtil.exists(code) && codes.add(code)) {// 判断授权码是否存在,避免重复生成
                DeviceRedisUtil.set(code, new DeviceCodeCache(request.getStoreId()));// 设置授权码缓存，有效期5分钟
            }
        }
        return codes;
    }

    /**
     * 删除设备
     *
     * @param deviceId 设备id
     */
    @Transactional
    public void deleteDevice(String deviceId) {
        int num = deviceRepository.softDeleteById(deviceId);
        log.info("删除设备：{}，结果：{}", deviceId, num);
    }
}
