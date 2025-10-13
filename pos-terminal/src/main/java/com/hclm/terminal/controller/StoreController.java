package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.web.ApiResponse;
import com.hclm.terminal.pojo.request.StoreRequest;
import com.hclm.terminal.pojo.response.StoreResponse;
import com.hclm.terminal.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@SaCheckLogin
@RestController
@Tag(name = "店铺管理控制器", description = "店铺的增删改查接口")
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "创建店铺")
    @PostMapping
    public ApiResponse<StoreResponse> createStore(@Valid @RequestBody StoreRequest requestDTO) {
        return ApiResponse.success(storeService.createStore(requestDTO));
    }

    @Operation(summary = "更新店铺")
    @PutMapping("/{id}")
    public ApiResponse<StoreResponse> updateStore(@PathVariable String id, @Valid @RequestBody StoreRequest requestDTO) {
        return ApiResponse.success(storeService.updateStore(id, requestDTO));
    }

    @Operation(summary = "删除店铺（软删除）")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStore(@PathVariable String id) {
        storeService.deleteStore(id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取单个店铺")
    @GetMapping("/{id}")
    public ApiResponse<StoreResponse> getStoreById(@PathVariable String id) {
        return ApiResponse.success(storeService.getStoreById(id));
    }

    @Operation(summary = "根据商户ID获取店铺列表")
    @GetMapping("/merchant/{merchantId}")
    public ApiResponse<List<StoreResponse>> getStoresByMerchantId(@PathVariable String merchantId) {
        return ApiResponse.success(storeService.getStoresByMerchantId(merchantId));
    }

    @Operation(summary = "获取全部店铺")
    @GetMapping
    public ApiResponse<List<StoreResponse>> getAllStores() {
        return ApiResponse.success(storeService.getAllStores());
    }

    @Operation(summary = "更新店铺状态")
    @PatchMapping("/{id}/status")
    public ApiResponse<StoreResponse> updateStatus(@PathVariable String id, @RequestParam String status) {
        return ApiResponse.success(storeService.updateStatus(id, status));
    }

    @Operation(summary = "更新营业时间")
    @PatchMapping("/{id}/business-hours")
    public ApiResponse<StoreResponse> updateBusinessHours(@PathVariable String id, @RequestParam String businessHours) {
        return ApiResponse.success(storeService.updateBusinessHours(id, businessHours));
    }
}

