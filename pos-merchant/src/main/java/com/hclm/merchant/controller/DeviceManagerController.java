package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.DeviceIDGenRequest;
import com.hclm.merchant.service.DeviceManagerService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Validated
@SaCheckLogin //检查登录
@Tag(name = "设备管理")
@RequiredArgsConstructor
@RequestMapping("/device")
@RestController
public class DeviceManagerController {
    private final DeviceManagerService deviceManagerService;

    /**
     * 生成授权代码
     *
     * @param request 请求
     * @return {@link ApiResponse }
     */
    @Operation(summary = "生成授权代码")
    @PostMapping("/gen-codes")
    public ApiResponse<Set<String>> genCodes(@RequestBody @Valid DeviceIDGenRequest request) {
        return ApiResponse.success(deviceManagerService.genCodes(request));
    }

    /**
     * 删除
     *
     * @param deviceId 设备id
     * @return {@link ApiResponse }<{@link Void }>
     */
    @Operation(summary = "解除设备绑定")
    @DeleteMapping("/{deviceId}")
    public ApiResponse<Void> delete(@PathVariable String deviceId) {
        deviceManagerService.deleteDevice(deviceId);
        return ApiResponse.success();
    }
}
