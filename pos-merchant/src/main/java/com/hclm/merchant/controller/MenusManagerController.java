package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.MenusAddRequest;
import com.hclm.merchant.pojo.request.MenusUpdateRequest;
import com.hclm.merchant.pojo.response.MenusResponse;
import com.hclm.merchant.service.MenusManagerService;
import com.hclm.tenant.Tenant;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理器控制器
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Tenant // 开启租户功能
@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜单管理")
@RequiredArgsConstructor
@RequestMapping("/menus")
@RestController
public class MenusManagerController {
    private final MenusManagerService menusManagerService;

    @Operation(summary = "添加菜单")
    @PostMapping("")
    public ApiResponse<MenusResponse> add(@Valid @RequestBody MenusAddRequest request) {
        return ApiResponse.success(menusManagerService.addMenus(request));
    }

    @Operation(summary = "更新菜单")
    @PutMapping("/{id}")
    public ApiResponse<MenusResponse> update(@PathVariable("id") Long id, @Valid @RequestBody MenusUpdateRequest request) {
        return ApiResponse.success(menusManagerService.updateMenus(id, request));
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        menusManagerService.removeById(id);
        return ApiResponse.success();
    }

    @Operation(summary = "查询菜单列表")
    @GetMapping("")
    public ApiResponse<List<MenusResponse>> list(@Schema(description = "所属门店id") @NotEmpty String storeId) {
        return ApiResponse.success(menusManagerService.listMenus(storeId));
    }


}
