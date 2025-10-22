package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.tenant.Tenant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tenant // 开启租户功能
@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜品管理")
@RequiredArgsConstructor
@RequestMapping("/items")
@RestController
public class ItemsMangerController {
}
