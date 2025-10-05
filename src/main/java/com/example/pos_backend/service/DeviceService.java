package com.example.pos_backend.service;

import com.example.pos_backend.common.*;
import com.example.pos_backend.dto.cache.DeviceCodeCache;
import com.example.pos_backend.dto.mapper.DeviceMapper;
import com.example.pos_backend.dto.request.DeviceAddRequestDTO;
import com.example.pos_backend.dto.request.DeviceIDGenRequestDTO;
import com.example.pos_backend.dto.response.DeviceAddResponse;
import com.example.pos_backend.entity.Device;
import com.example.pos_backend.exception.BusinessException;
import com.example.pos_backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class DeviceService {
    private final DeviceRepository deviceRepository;

    /**
     * 生成代码
     *
     * @param request 请求
     * @return {@link List }<{@link String }>
     */
    public Set<String> genCodes(DeviceIDGenRequestDTO request) {
        int quantity = request.getQuantity();// 数量
        Set<String> codes = HashSet.newHashSet(quantity);
        while (codes.size() < quantity) {// 生成数量等于数量时，结束循环
            String code = RandomUtil.upperAndNumbers(12); // 生成设备授权码，12位，大写字母和数字
            if (!RedisUtil.exists("devicecode:" + code) && codes.add(code)) {// 判断授权码是否存在,避免重复生成
                RedisUtil.set("devicecode:" + code, new DeviceCodeCache(request.getStoreId()), Duration.ofMinutes(5));// 设置授权码缓存，有效期5分钟
            }
        }
        return codes;
    }

    /**
     * 添加设备
     *
     * @param request 请求
     */
    public DeviceAddResponse addDevice(DeviceAddRequestDTO request) {
        DeviceCodeCache cache = RedisUtil.getCache("devicecode:" + request.getCode(), DeviceCodeCache.class);
        if (cache == null) {
            throw new BusinessException(MessageUtil.getMessage("devicecode.notFound"));
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

    private Device findByDeviceId(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BusinessException(MessageUtil.getMessage("deviceid.notFound")));
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
