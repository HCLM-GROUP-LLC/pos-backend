package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.redis.DeviceRedisUtil;
import com.hclm.redis.cache.DeviceCodeCache;
import com.hclm.terminal.converter.DeviceConverter;
import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
import com.hclm.terminal.service.DeviceService;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Device;
import com.hclm.web.enums.ClientTypeEnum;
import com.hclm.web.enums.DeviceStatusEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.mapper.DeviceMapper;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {
    @Override
    @NonNull
    public Device findByDeviceId(String deviceId) {
        return getOptById(deviceId)
                .orElseThrow(() -> new BusinessException(ResponseCode.DEVICE_ID_NOT_FOUND));
    }

    /**
     * 添加设备
     *
     * @param request 请求
     * @return {@link DeviceAddResponse }
     */
    @Override
    public DeviceAddResponse addDevice(DeviceAddRequest request) {
        DeviceCodeCache cache = DeviceRedisUtil.get(request.getCode());
        if (cache == null) {
            throw new BusinessException(ResponseCode.DEVICECODE_NOTFOUND);
        }
        // 删除缓存
        DeviceRedisUtil.delete(request.getCode());
        // 插入数据库
        Device device = DeviceConverter.INSTANCE.toEntity(request);
        if (!StringUtils.hasLength(device.getDeviceName())) {// 设备名称为空,自动生成名称
            device.setDeviceName("Device" + DeviceRedisUtil.getDeviceNumberNext(cache.getStoreId()));
        }
        device.setDeviceId(RandomUtil.generateDeviceId());
        device.setMerchantId(cache.getMerchantId());
        device.setStoreId(cache.getStoreId());// 设置门店id
        device.setRegisteredAt(System.currentTimeMillis());// 设置注册时间
        device.setDeviceType(ClientTypeEnum.IOS.name());
        save(device);// 保存设备
        return DeviceConverter.INSTANCE.toAddResponse(device);
    }

    /**
     * 登录
     *
     * @param deviceId 设备id
     */
    @Override
    public void login(String deviceId) {
        boolean update = lambdaUpdate()
                .set(Device::getStatus, DeviceStatusEnum.ONLINE)
                .set(Device::getLastLoginAt, System.currentTimeMillis()) // 更新最后登录时间
                .eq(Device::getDeviceId, deviceId)
                .update();//更新数据
        log.info("设备登录：{}，结果：{}", deviceId, update);
    }

    /**
     * 在线
     *
     * @param deviceId 设备id
     */
    @Override
    public void online(String deviceId) {
        boolean update = lambdaUpdate()
                .set(Device::getStatus, DeviceStatusEnum.ONLINE)
                .set(Device::getLastOnline, System.currentTimeMillis()) // 更新最后登录时间
                .eq(Device::getDeviceId, deviceId)
                .update();//更新数据
        log.info("设备上线：{}，结果：{}", deviceId, update);
    }

    /**
     * 离线
     *
     * @param deviceId 设备id
     */
    @Override
    public void offline(String deviceId) {
        boolean update = lambdaUpdate()
                .set(Device::getStatus, DeviceStatusEnum.OFFLINE)
                .eq(Device::getDeviceId, deviceId)
                .update();
        log.info("设备离线：{}，结果：{}", deviceId, update);
    }
}
