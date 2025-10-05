package com.example.pos_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备添加请求dto
 *
 * @author hanhua
 * @since 2025/10/05
 */
@Data
public class DeviceAddRequestDTO {
    /**
     * 设备授权码
     */
    @NotNull(message = "{devicecode.notNull}")
    @NotEmpty(message = "{devicecode.notNull}")
    @Schema(description = "设备授权码")
    private String code;
    /**
     * 设备名称
     */
    @NotNull(message = "{devicename.notNull}")
    @NotEmpty(message = "{devicename.notNull}")
    @Schema(description = "设备名称")
    private String deviceName;
    /**
     * mac地址
     */
    @NotNull(message = "{macaddress.notNull}")
    @NotEmpty(message = "{macaddress.notNull}")
    @Schema(description = "设备MAC地址")
    private String macAddress;
    /**
     * ip地址
     */
    @NotNull(message = "{ipaddress.notNull}")
    @NotEmpty(message = "{ipaddress.notNull}")
    @Schema(description = "设备IP地址")
    private String ipAddress;
}
