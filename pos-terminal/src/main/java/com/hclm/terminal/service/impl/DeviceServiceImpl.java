package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.DeviceEntity;
import com.hclm.mybatis.enums.DeviceStatusEnum;
import com.hclm.mybatis.mapper.DeviceMapper;
import com.hclm.redis.DeviceRedisUtil;
import com.hclm.redis.cache.DeviceCodeCache;
import com.hclm.terminal.converter.DeviceConverter;
import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
import com.hclm.terminal.service.DeviceService;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ClientTypeEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {
    @Override
    @NonNull
    public DeviceEntity findByDeviceId(String deviceId) {
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
        DeviceEntity deviceEntity = DeviceConverter.INSTANCE.toEntity(request);
        if (!StringUtils.hasLength(deviceEntity.getDeviceName())) {// 设备名称为空,自动生成名称
            deviceEntity.setDeviceName("DeviceEntity" + DeviceRedisUtil.getDeviceNumberNext(cache.getStoreId()));
        }
        deviceEntity.setDeviceId(RandomUtil.generateDeviceId());
        deviceEntity.setMerchantId(cache.getMerchantId());
        deviceEntity.setStoreId(cache.getStoreId());// 设置门店id
        deviceEntity.setRegisteredAt(System.currentTimeMillis());// 设置注册时间
        deviceEntity.setDeviceType(ClientTypeEnum.IOS.name());
        save(deviceEntity);// 保存设备
        return DeviceConverter.INSTANCE.toAddResponse(deviceEntity);
    }

    /**
     * 登录
     *
     * @param deviceId 设备id
     */
    @Override
    public void login(String deviceId) {
        boolean update = lambdaUpdate()
                .set(DeviceEntity::getStatus, DeviceStatusEnum.ONLINE)
                .set(DeviceEntity::getLastLoginAt, System.currentTimeMillis()) // 更新最后登录时间
                .eq(DeviceEntity::getDeviceId, deviceId)
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
                .set(DeviceEntity::getStatus, DeviceStatusEnum.ONLINE)
                .set(DeviceEntity::getLastOnline, System.currentTimeMillis()) // 更新最后登录时间
                .eq(DeviceEntity::getDeviceId, deviceId)
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
                .set(DeviceEntity::getStatus, DeviceStatusEnum.OFFLINE)
                .eq(DeviceEntity::getDeviceId, deviceId)
                .update();
        log.info("设备离线：{}，结果：{}", deviceId, update);
    }
}
