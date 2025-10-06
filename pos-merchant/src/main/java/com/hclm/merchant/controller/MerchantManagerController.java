package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
}
