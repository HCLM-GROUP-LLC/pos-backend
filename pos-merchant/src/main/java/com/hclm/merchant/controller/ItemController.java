package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.ItemRequest;
import com.hclm.merchant.pojo.response.ItemResponse;
import com.hclm.merchant.service.ItemService;
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
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * 创建菜品
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@RequestBody ItemRequest request) {
        ItemResponse response = itemService.createItem(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 更新菜品
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(@PathVariable String itemId,
                                                                @RequestBody ItemRequest request) {
        ItemResponse response = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 删除菜品（逻辑删除）
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable String itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 更新菜品状态（可选接口）
     */
    @PatchMapping("/{itemId}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable String itemId,
                                                          @RequestParam String status) {
        itemService.updateStatus(itemId, status);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
