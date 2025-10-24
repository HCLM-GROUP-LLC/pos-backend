package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.DeviceIDGenRequest;
import com.hclm.mybatis.entity.DeviceEntity;

import java.util.Set;

/**
 * 设备管理器服务
 *
 * @author hanhua
 */
public interface DeviceManagerService extends IService<DeviceEntity> {
    /**
     * gen代码
     *
     * @param request 请求
     * @return {@link Set }<{@link String }>
     */
    Set<String> genCodes(DeviceIDGenRequest request);
}
