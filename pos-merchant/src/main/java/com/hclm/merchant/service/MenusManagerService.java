package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.MenusAddRequest;
import com.hclm.merchant.pojo.request.MenusUpdateRequest;
import com.hclm.merchant.pojo.response.MenusResponse;
import com.hclm.web.entity.Menus;

import java.util.List;

/**
 * 菜单管理器服务
 *
 * @author hanhua
 * @since 2025/10/22
 */
public interface MenusManagerService extends IService<Menus> {
    /**
     * 添加菜单
     *
     * @param request 请求
     * @return {@link MenusResponse }
     */
    MenusResponse addMenus(MenusAddRequest request);

    /**
     * 更新菜单
     *
     * @param request 请求
     * @return {@link MenusResponse }
     */
    MenusResponse updateMenus(Long menuId, MenusUpdateRequest request);

    /**
     * 列表菜单
     *
     * @param storeId 门店id
     * @return {@link List }<{@link MenusResponse }>
     */
    List<MenusResponse> listMenus(String storeId);
}
