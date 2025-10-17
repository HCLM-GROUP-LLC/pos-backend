package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.MenuRequest;
import com.hclm.merchant.pojo.response.MenuResponse;
import com.hclm.merchant.service.MenuService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜单管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    /**
     * 创建菜单
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(@RequestBody MenuRequest request) {
        MenuResponse response = menuService.createMenu(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 更新菜单信息
     */
    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(@PathVariable String menuId,
                                                                @RequestBody MenuRequest request) {
        MenuResponse response = menuService.updateMenu(menuId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 删除菜单（逻辑删除）
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable String menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 修改菜单是否激活状态
     */
    @PatchMapping("/{menuId}/isActive")
    public ResponseEntity<ApiResponse<Void>> updateIsActive(@PathVariable String menuId,
                                                            @RequestParam Boolean isActive) {
        menuService.updateIsActive(menuId, isActive);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}

