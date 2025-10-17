package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.MenuCategoryItemRequest;
import com.hclm.merchant.pojo.response.MenuCategoryItemResponse;
import com.hclm.merchant.service.MenuCategoryItemService;
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
@RequestMapping("menu-category-items")
public class MenuCategoryItemController {

    private final MenuCategoryItemService mciService;

    /**
     * 绑定菜品到菜单分类
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MenuCategoryItemResponse>> bindItemToCategory(@RequestBody MenuCategoryItemRequest request) {
        MenuCategoryItemResponse response = mciService.bindItemToCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 删除菜单分类与菜品的绑定关系
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBinding(@PathVariable String id) {
        mciService.deleteBinding(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
