package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.hclm.terminal.pojo.request.DeviceAddRequest;
import com.hclm.terminal.pojo.response.DeviceAddResponse;
import com.hclm.terminal.service.DeviceService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@SaCheckLogin //检查登录
@Tag(name = "设备")
@RequiredArgsConstructor
@RequestMapping("/devices")
@RestController
public class DeviceController {
    private final DeviceService deviceService;

    /**
     * 添加设备
     *
     * @param request 请求
     * @return {@link ApiResponse }<{@link Void }>
     */
    @SaIgnore // 忽略登录
    @Operation(summary = "添加设备")
    @PostMapping("/")
    public ApiResponse<DeviceAddResponse> add(@RequestBody @Valid DeviceAddRequest request) {
        return ApiResponse.success(deviceService.addDevice(request));
    }
}
