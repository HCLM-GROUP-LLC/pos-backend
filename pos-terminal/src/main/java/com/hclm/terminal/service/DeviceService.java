package com.hclm.terminal.service;

import com.hclm.redis.DeviceRedisUtil;
import com.hclm.redis.cache.DeviceCodeCache;
import com.hclm.terminal.mapper.DeviceMapper;
import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Device;
import com.hclm.web.enums.ClientTypeEnum;
import com.hclm.web.enums.DeviceStatusEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.repository.DeviceRepository;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceService {
    private final DeviceRepository deviceRepository;

    @NonNull
    public Device findByDeviceId(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BusinessException(ResponseCode.DEVICE_ID_NOT_FOUND));
    }

    /**
     * 添加设备
     *
     * @param request 请求
     */
    @Transactional
    public DeviceAddResponse addDevice(DeviceAddRequest request) {
        DeviceCodeCache cache = DeviceRedisUtil.get(request.getCode());
        if (cache == null) {
            throw new BusinessException(ResponseCode.DEVICECODE_NOTFOUND);
        }
        // 删除缓存
        DeviceRedisUtil.delete(request.getCode());
        // 插入数据库
        Device device = DeviceMapper.INSTANCE.toEntity(request);
        device.setDeviceId(RandomUtil.generateDeviceId());
        device.setStoreId(cache.getStoreId());// 设置门店id
        device.setRegisteredAt(System.currentTimeMillis());// 设置注册时间
        device.setDeviceType(ClientTypeEnum.IOS.name());
        deviceRepository.save(device);// 保存设备
        return DeviceMapper.INSTANCE.toAddResponse(device);
    }

    @Transactional
    public void login(String deviceId) {
        int num = deviceRepository.updateStatusAndLastLoginAt(deviceId, DeviceStatusEnum.ONLINE.name(), System.currentTimeMillis());//更新数据
        log.info("设备登录：{}，结果：{}", deviceId, num);
    }

    /**
     * 设置在线设备
     *
     * @param deviceId 设备id
     */
    @Transactional
    public void online(String deviceId) {
        int num = deviceRepository.updateStatusAndLastOnline(deviceId, DeviceStatusEnum.ONLINE.name(), System.currentTimeMillis());//更新数据
        log.info("设备上线：{}，结果：{}", deviceId, num);
    }

    /**
     * 离线
     *
     * @param deviceId 设备id
     */
    @Transactional
    public void offline(String deviceId) {
        deviceRepository.updateStatus(deviceId, DeviceStatusEnum.OFFLINE.name());//更新数据
    }
}
