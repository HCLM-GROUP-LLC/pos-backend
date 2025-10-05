package com.example.pos_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceOnlineReques {
    @NotNull(message = "{deviceid.notNull}")
    @NotEmpty(message = "{deviceid.notNull}")
    @Schema(description = "设备ID")
    private String deviceId;
}
