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
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class DeviceService {
    private final DeviceRepository deviceRepository;

    private Device findByDeviceId(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BusinessException(ResponseCode.DEVICECODE_NOTFOUND));
    }

    /**
     * 添加设备
     *
     * @param request 请求
     */
    public DeviceAddResponse addDevice(DeviceAddRequest request) {
        DeviceCodeCache cache = DeviceRedisUtil.get(request.getCode());
        if (cache == null) {
            throw new BusinessException(ResponseCode.DEVICECODE_NOTFOUND);
        }
        Device device = DeviceMapper.INSTANCE.toEntity(request);
        device.setDeviceId(RandomUtil.uuid());
        device.setStoreId(cache.getStoreId());// 设置门店id
        device.setLastOnline(Instant.now());// 设置最后在线时间
        device.setRegisteredAt(Instant.now());// 设置注册时间
        device.setDeviceType(ClientTypeEnum.IOS.getCode());
        device.setStatus(DeviceStatusEnum.ONLINE.getCode());
        deviceRepository.save(device);// 保存设备
        // 设备上线
        online(device.getDeviceId());
        return DeviceMapper.INSTANCE.toAddResponse(device);
    }

    /**
     * 设置在线设备
     *
     * @param deviceId 设备id
     */
    public void online(String deviceId) {
        Device device = findByDeviceId(deviceId);
        device.setStatus(DeviceStatusEnum.ONLINE.getCode());
        device.setLastOnline(Instant.now());// 设置最后在线时间
        deviceRepository.save(device);//更新数据
    }

    /**
     * 离线
     *
     * @param deviceId 设备id
     */
    public void offline(String deviceId) {
        Device device = findByDeviceId(deviceId);
        device.setStatus(DeviceStatusEnum.OFFLINE.getCode());
        deviceRepository.save(device);//更新数据
    }
}
