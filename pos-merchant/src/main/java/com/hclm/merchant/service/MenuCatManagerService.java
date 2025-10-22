package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.MenuCatAddRequest;
import com.hclm.merchant.pojo.request.MenuCatUpdateRequest;
import com.hclm.merchant.pojo.response.MenuCatResponse;
import com.hclm.web.entity.MenuCategories;

import java.util.List;

public interface MenuCatManagerService extends IService<MenuCategories> {
    /**
     * 添加
     *
     * @param request 请求
     * @return {@link MenuCatResponse }
     */
    MenuCatResponse add(MenuCatAddRequest request);

    /**
     * 更新
     *
     * @param categoryId 类别id
     * @param request    请求
     * @return {@link MenuCatResponse }
     */
    MenuCatResponse update(Long categoryId, MenuCatUpdateRequest request);

    /**
     * 列表
     *
     * @param menuId 菜单id
     * @return {@link List }<{@link MenuCatResponse }>
     */
    List<MenuCatResponse> list(String menuId);

}
