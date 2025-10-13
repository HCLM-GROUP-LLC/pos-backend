package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.hclm.merchant.pojo.request.MerchantLoginRequest;
import com.hclm.merchant.pojo.request.MerchantRegisterRequest;
import com.hclm.merchant.pojo.response.AuthResponse;
import com.hclm.merchant.service.MerchantService;
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

/**
 * 商家授权控制器
 *
 * @author hanhua
 * @since 2025/10/06
 */
@Validated
@SaIgnore //忽略权限控制
@Tag(name = "授权")
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class MerchantAuthController {
    private final MerchantService merchantService;

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link ApiResponse }<{@link Void }>
     */
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid MerchantLoginRequest request) {
        return ApiResponse.success(merchantService.login(request));
    }

    /**
     * 注册
     *
     * @param request 请求
     * @return {@link ApiResponse }<{@link Void }>
     */
    @Operation(summary = "注册")
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@RequestBody @Valid MerchantRegisterRequest request) {
        return ApiResponse.success(merchantService.register(request));
    }
}
