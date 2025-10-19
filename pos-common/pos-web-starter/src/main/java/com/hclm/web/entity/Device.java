package com.hclm.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hclm.web.constant.TableNameConstant;
import com.hclm.web.enums.DeviceStatusEnum;
import lombok.Data;

@Data
@TableName(TableNameConstant.DEVICES)
public class Device {
    @TableId(type = IdType.INPUT)
    private String deviceId;

    /**
     * 商户id
     */
    private String merchantId;
    private String storeId;

    private String deviceName;

    private String deviceType;

    private String macAddress;

    private String ipAddress;

    /**
     * 上次联机 毫秒级时间戳
     */
    private Long lastOnline;

    private DeviceStatusEnum status = DeviceStatusEnum.OFFLINE;

    /**
     * 注册于 毫秒级时间戳
     */
    @TableField(fill = FieldFill.INSERT)
    private Long registeredAt;

    /**
     * 上次登录于 毫秒级时间戳
     */
    private Long lastLoginAt;

    @TableLogic // 逻辑删除
    private Boolean isDeleted = false;

}