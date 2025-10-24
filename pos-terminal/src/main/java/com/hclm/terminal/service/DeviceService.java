package com.hclm.terminal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.mybatis.entity.DeviceEntity;
import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
import org.springframework.lang.NonNull;

public interface DeviceService extends IService<DeviceEntity> {


    /**
     * 按设备id查找 找不到抛出异常
     *
     * @param deviceId 设备id
     * @return {@link DeviceEntity }
     */
    @NonNull
    DeviceEntity findByDeviceId(String deviceId);

    /**
     * 添加设备
     *
     * @param request 请求
     */
    DeviceAddResponse addDevice(DeviceAddRequest request);

    /**
     * 登录
     *
     * @param deviceId 设备id
     */
    void login(String deviceId);

    /**
     * 设置在线设备
     *
     * @param deviceId 设备id
     */
    void online(String deviceId);

    /**
     * 离线
     *
     * @param deviceId 设备id
     */
    void offline(String deviceId);
}
