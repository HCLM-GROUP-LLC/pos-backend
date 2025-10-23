package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.CatItemsAddRequest;
import com.hclm.merchant.service.MenuCatItemsService;
import com.hclm.tenant.Tenant;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单分类控制器
 *
 * @author hanhua
 * @since 2025/10/23
 */
@Tenant // 开启租户功能
@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜品与分类的关联")
@RequiredArgsConstructor
@RequestMapping("/menu-cat-item")
@RestController
public class MenuCatItemController {
    private final MenuCatItemsService menuCatItemsService;

    /**
     * 添加
     *
     * @param request 请求
     */
    @Operation(summary = "添加菜品与分类的关联")
    @PostMapping
    public ApiResponse<Void> add(@RequestBody @Valid CatItemsAddRequest request) {
        menuCatItemsService.add(request);
        return ApiResponse.success();
    }

    /**
     * 删除
     *
     * @param request 删除请求
     */
    @Operation(summary = "删除菜品与分类的关联")
    @DeleteMapping
    public ApiResponse<Void> remove(@RequestBody @Valid CatItemsAddRequest request) {
        menuCatItemsService.remove(request);
        return ApiResponse.success();
    }
}
