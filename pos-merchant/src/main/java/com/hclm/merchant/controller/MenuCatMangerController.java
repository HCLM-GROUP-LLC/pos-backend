package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.MenuCatAddRequest;
import com.hclm.merchant.pojo.request.MenuCatUpdateRequest;
import com.hclm.merchant.pojo.response.MenuCatResponse;
import com.hclm.merchant.service.MenuCatManagerService;
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
 * 菜单分类类别管理器控制器
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Tenant // 开启租户功能
@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜单分类管理")
@RequiredArgsConstructor
@RequestMapping("/menu-categories")
@RestController
public class MenuCatMangerController {
    private final MenuCatManagerService menuCatManagerServce;

    @Operation(summary = "添加菜单分类")
    @PostMapping("")
    public ApiResponse<MenuCatResponse> add(@Valid @RequestBody MenuCatAddRequest request) {
        return ApiResponse.success(menuCatManagerServce.add(request));
    }

    @Operation(summary = "更新菜单分类")
    @PutMapping("/{id}")
    public ApiResponse<MenuCatResponse> update(@PathVariable("id") Long id, @Valid @RequestBody MenuCatUpdateRequest request) {
        return ApiResponse.success(menuCatManagerServce.update(id, request));
    }

    @Operation(summary = "删除菜单分类")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        menuCatManagerServce.removeById(id);
        return ApiResponse.success();
    }

    @Operation(summary = "查询菜单分类列表")
    @GetMapping("")
    public ApiResponse<List<MenuCatResponse>> list(@Schema(description = "所属菜单id") @NotEmpty String menuId) {
        return ApiResponse.success(menuCatManagerServce.list(menuId));
    }

}
