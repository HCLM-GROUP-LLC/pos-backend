package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.pojo.request.DeviceIDGenRequest;
import com.hclm.merchant.service.DeviceManagerService;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.mybatis.entity.DeviceEntity;
import com.hclm.mybatis.mapper.DeviceMapper;
import com.hclm.redis.DeviceRedisUtil;
import com.hclm.redis.cache.DeviceCodeCache;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceManagerServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceManagerService {

    /**
     * 生成代码
     *
     * @param request 请求
     * @return {@link List }<{@link String }>
     */
    public Set<String> genCodes(DeviceIDGenRequest request) {
        int quantity = request.getQuantity();// 数量
        Set<String> codes = new HashSet<>(quantity);
        String merchantId = MerchantLoginUtil.getMerchantId();
        while (codes.size() < quantity) {// 生成数量等于数量时，结束循环
            String code = RandomUtil.upperAndNumbers(12); // 生成设备授权码，12位，大写字母和数字
            if (!DeviceRedisUtil.exists(code) && codes.add(code)) {// 判断授权码是否存在,避免重复生成
                DeviceCodeCache cache = new DeviceCodeCache();
                cache.setStoreId(request.getStoreId());
                cache.setMerchantId(merchantId);
                DeviceRedisUtil.set(code, cache);// 设置授权码缓存，有效期5分钟
            }
        }
        return codes;
    }

}
