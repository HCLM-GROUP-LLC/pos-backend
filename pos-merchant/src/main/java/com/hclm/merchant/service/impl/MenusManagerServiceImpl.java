package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.MenusConverter;
import com.hclm.merchant.pojo.request.MenusAddRequest;
import com.hclm.merchant.pojo.request.MenusUpdateRequest;
import com.hclm.merchant.pojo.response.MenusResponse;
import com.hclm.merchant.service.MenusManagerService;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Menus;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.mapper.MenusMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单管理实现
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Service
public class MenusManagerServiceImpl extends ServiceImpl<MenusMapper, Menus> implements MenusManagerService {
    private void checkNameUnique(Menus menus) {
        if (lambdaQuery()
                .eq(Menus::getStoreId, menus.getStoreId())
                .eq(Menus::getMenuName, menus.getMenuName())
                .ne(menus.getMenuId() != null, Menus::getMenuId, menus.getMenuId()) // 排除自身
                .exists()) {
            throw new BusinessException(ResponseCode.MENU_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * 添加菜单
     *
     * @param request 请求
     * @return {@link MenusResponse }
     */
    @Override
    public MenusResponse addMenus(MenusAddRequest request) {
        Menus menus = MenusConverter.INSTANCE.toEntity(request);
        checkNameUnique(menus);
        save(menus);//保存
        return MenusConverter.INSTANCE.toResponse(menus);
    }

    @Override
    public MenusResponse updateMenus(Long menuId, MenusUpdateRequest request) {
        Menus oldMenus = getOptById(menuId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MENU_NOT_FOUND));
        //新的值复制到旧的值
        MenusConverter.INSTANCE.copy(request, oldMenus);
        checkNameUnique(oldMenus); // 检查名称唯一性

        Menus menus = MenusConverter.INSTANCE.toEntity(request);
        menus.setMenuId(menuId);
        updateById(menus);//会跳过null字段

        return MenusConverter.INSTANCE.toResponse(oldMenus);
    }

    @Override
    public List<MenusResponse> listMenus(String storeId) {

        return MenusConverter.INSTANCE.toResponse(
                lambdaQuery()
                        .eq(Menus::getStoreId, storeId)
                        .list()
        );
    }
}
