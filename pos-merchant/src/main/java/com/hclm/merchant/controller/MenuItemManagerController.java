package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.MenuItemAddRequest;
import com.hclm.merchant.pojo.request.MenuItemUpdateRequest;
import com.hclm.merchant.pojo.response.MenuItemResponse;
import com.hclm.merchant.service.MenuItemManagerService;
import com.hclm.tenant.Tenant;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单项管理器控制器
 *
 * @author hanhua
 * @since 2025/10/23
 */
@Tenant // 开启租户功能
@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜品管理")
@RequiredArgsConstructor
@RequestMapping("/menu-items")
@RestController
public class MenuItemManagerController {
    private final MenuItemManagerService menuItemManagerService;

    /**
     * 查找由门店id 在 Item Design 页面中调用
     *
     * @param storeId 门店id
     * @return {@link ApiResponse }<{@link List }<{@link MenuItemResponse }>>
     */
    @Operation(summary = "查找由门店id")
    @GetMapping("/store")
    public ApiResponse<List<MenuItemResponse>> findByStoreId(@NotEmpty @Schema(description = "门店id") String storeId) {
        return ApiResponse.success(menuItemManagerService.findByStoreId(storeId));
    }

    /**
     * 按类别id查找 在 Menu Design 页面中调用
     *
     * @param categoryId 类别id
     * @return {@link ApiResponse }<{@link List }<{@link MenuItemResponse }>>
     */
    @Operation(summary = "按类别id查找")
    @GetMapping("/category")
    public ApiResponse<List<MenuItemResponse>> findByCategoryId(@NotNull @Schema(description = "类别id") Long categoryId) {
        return ApiResponse.success(menuItemManagerService.findByCategoryId(categoryId));
    }
    /*
     * 文档没有识别出来这里是 multipart/form-data 可以使用curl命令 以下是 示例
     * curl -X POST \
     * http://localhost:8080/menu-items \
     * -H "Content-Type: multipart/form-data" \
     * -H "pos-merchant-token: 48341aa1-7791-4f55-a9a4-d5b1fd4ab39f" \
     * -F "storeId=LOC-174000000001" \
     * -F "itemName=测试菜品" \
     * -F "itemDescription=这是一道测试菜品" \
     * -F "itemPrice=29.99" \
     * -F "itemType=Item" \
     * -F "itemImage=@/mnt/d/Users/hanhua/Downloads/1.png"
     * */

    /**
     * 创建菜品 在 Item Design 创建菜品时调用
     *
     * @param request 创建菜品请求
     * @return {@link ApiResponse }<{@link MenuItemResponse }>
     */
    @Operation(summary = "创建菜品")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<MenuItemResponse> create(@Valid @ModelAttribute MenuItemAddRequest request) {
        return ApiResponse.success(menuItemManagerService.create(request, request.getItemImage()));
    }

    /**
     * 更新菜品 在 Item Design 更新菜品时调用
     *
     * @param request 更新菜品请求
     * @return {@link ApiResponse }<{@link MenuItemResponse }>
     */
    @Operation(summary = "更新菜品")
    @PutMapping("/{menuItemId}")
    public ApiResponse<MenuItemResponse> update(@PathVariable Long menuItemId, @Valid @RequestBody MenuItemUpdateRequest request) {
        return ApiResponse.success(menuItemManagerService.update(menuItemId, request));
    }

    /**
     * 删除菜品 在 Item Design 删除菜品时调用
     *
     * @param menuItemId 菜品id
     * @return {@link ApiResponse }<{@link Void }>
     */
    @Operation(summary = "删除菜品")
    @DeleteMapping("/{menuItemId}")
    public ApiResponse<Void> delete(@PathVariable Long menuItemId) {
        menuItemManagerService.removeById(menuItemId);
        return ApiResponse.success();
    }
}
