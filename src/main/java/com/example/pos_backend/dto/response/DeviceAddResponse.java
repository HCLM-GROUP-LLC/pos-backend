package com.example.pos_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备添加响应
 *
 * @author hanhua
 * @since 2025/10/05
 */
@Data
public class DeviceAddResponse {
    @Schema(description = "设备ID")
    private String deviceId;
}
