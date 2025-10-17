package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.MenuCategoryRequest;
import com.hclm.merchant.pojo.response.MenuCategoryResponse;
import com.hclm.merchant.service.MenuCategoryService;
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
@RequestMapping("menu-categories")
public class MenuCategoryController {

    private final MenuCategoryService categoryService;

    /**
     * 创建菜单分类
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> createCategory(@RequestBody MenuCategoryRequest request) {
        MenuCategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 更新菜单分类
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> updateCategory(@PathVariable String categoryId,
                                                                            @RequestBody MenuCategoryRequest request) {
        MenuCategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 删除菜单分类
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 更新菜单分类的基本信息（名称、描述、排序）
     */
    @PatchMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCategoryInfo(@PathVariable String categoryId,
                                                                @RequestParam String name,
                                                                @RequestParam String description,
                                                                @RequestParam Integer sortOrder) {
        categoryService.updateCategoryInfo(categoryId, name, description, sortOrder);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}


