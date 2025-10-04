package com.example.pos_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.pos_backend.common.ApiResponse;
import com.example.pos_backend.dto.request.DeviceIDGenRequestDTO;
import com.example.pos_backend.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 设备控制器
 *
 * @author hanhua
 * @since 2025/10/03
 */
@SaCheckLogin // 需要登录
@Tag(name = "设备控制器", description = "设备控制器")
@Validated // 开启数据验证
@Slf4j
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    /**
     * 生成授权代码
     *
     * @param request 请求
     * @return {@link ApiResponse }
     */
    @Operation(summary = "生成授权代码")
    @PostMapping("/gen-codes")
    public ApiResponse<Set<String>> genCodes(@RequestBody @Valid DeviceIDGenRequestDTO request) {
        return ApiResponse.success(deviceService.genCodes(request));
    }

}
