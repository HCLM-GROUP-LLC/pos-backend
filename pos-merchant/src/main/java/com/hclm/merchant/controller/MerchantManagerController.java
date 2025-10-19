package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.converter.MerchantConverter;
import com.hclm.merchant.pojo.response.MerchantInfoResponse;
import com.hclm.merchant.service.MerchantService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户管理控制器
 *
 * @author hanhua
 * @since 2025/10/07
 */
@Validated
@SaCheckLogin //检查登录
@Tag(name = "商户管理")
@RequiredArgsConstructor
@RequestMapping("/manager")
@RestController
public class MerchantManagerController {
    private final MerchantService merchantService;

    /**
     * 获取当前商家信息
     */
    @Operation(summary = "获取当前商家信息")
    @GetMapping("")
    public ApiResponse<MerchantInfoResponse> getMerchantInfo() {
        MerchantInfoResponse response =
                MerchantConverter.INSTANCE.toInfoResponse(merchantService.getMerchantInfo());
        return ApiResponse.success(response);
    }

}
