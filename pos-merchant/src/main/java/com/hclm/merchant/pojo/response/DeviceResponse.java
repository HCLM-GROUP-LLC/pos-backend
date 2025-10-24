package com.hclm.merchant.pojo.response;

import com.hclm.mybatis.enums.DeviceStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeviceResponse {
    @Schema(description = "设备id")
    private String deviceId;
    @Schema(description = "门店id")
    private String storeId;
    @Schema(description = "设备名称")
    private String deviceName;
    @Schema(description = "设备类型")
    private String deviceType;
    @Schema(description = "MAC地址")
    private String macAddress;
    @Schema(description = "IP地址")
    private String ipAddress;
    @Schema(description = "最后上线时间")
    private Long lastOnline;
    @Schema(description = "设备状态")
    private DeviceStatusEnum status;
    @Schema(description = "注册时间")
    private Long registeredAt;
    @Schema(description = "最后登录时间")
    private Long lastLoginAt;

}
