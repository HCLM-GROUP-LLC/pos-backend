package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.MenuItemAddRequest;
import com.hclm.merchant.pojo.request.MenuItemUpdateRequest;
import com.hclm.merchant.pojo.response.MenuItemResponse;
import com.hclm.web.entity.MenuItems;

import java.util.List;

public interface MenuItemManagerService extends IService<MenuItems> {
    /**
     * 获取某个门店下的所有菜单项
     *
     * @param storeId 门店id
     * @return {@link List }<{@link MenuItemResponse }>
     */
    List<MenuItemResponse> findByStoreId(String storeId);

    /**
     * 按类别id查找
     *
     * @param categoryId 类别id
     * @return {@link List }<{@link MenuItemResponse }>
     */
    List<MenuItemResponse> findByCategoryId(Long categoryId);

    /**
     * 创建
     *
     * @param request 请求
     * @return {@link MenuItemResponse }
     */
    MenuItemResponse create(MenuItemAddRequest request);

    /**
     * 更新
     *
     * @param menuItemId 菜单项id
     * @param request    请求
     * @return {@link MenuItemResponse }
     */
    MenuItemResponse update(Long menuItemId, MenuItemUpdateRequest request);
}
