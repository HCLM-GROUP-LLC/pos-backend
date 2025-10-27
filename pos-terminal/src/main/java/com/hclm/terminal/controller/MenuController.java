package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.tenant.Tenant;
import com.hclm.terminal.pojo.response.MenuResponse;
import com.hclm.terminal.service.MenuService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 菜单控制器
 *
 * @author hanhua
 * @since 2025/10/23
 */
@Validated
@SaCheckLogin //检查登录
@Tenant //开启租户功能
@Tag(name = "菜单")
@RequiredArgsConstructor
@RequestMapping("/menus")
@RestController
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "菜单列表")
    @GetMapping("")
    public ApiResponse<Collection<MenuResponse>> list() {
        return ApiResponse.success(menuService.queryAllMenus());
    }
}
