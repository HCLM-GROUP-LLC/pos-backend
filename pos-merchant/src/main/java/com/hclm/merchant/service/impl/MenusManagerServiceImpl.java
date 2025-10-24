package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.MenusConverter;
import com.hclm.merchant.pojo.request.MenusAddRequest;
import com.hclm.merchant.pojo.request.MenusUpdateRequest;
import com.hclm.merchant.pojo.response.MenusResponse;
import com.hclm.merchant.service.MenusManagerService;
import com.hclm.mybatis.entity.MenuEntity;
import com.hclm.mybatis.mapper.MenuMapper;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单管理实现
 *
 * @author hanhua
 * @since 2025/10/22
 */
@Service
public class MenusManagerServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenusManagerService {
    private void checkNameUnique(MenuEntity menuEntity) {
        if (lambdaQuery()
                .eq(MenuEntity::getStoreId, menuEntity.getStoreId())
                .eq(MenuEntity::getMenuName, menuEntity.getMenuName())
                .ne(menuEntity.getMenuId() != null, MenuEntity::getMenuId, menuEntity.getMenuId()) // 排除自身
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
        MenuEntity menuEntity = MenusConverter.INSTANCE.toEntity(request);
        checkNameUnique(menuEntity);
        save(menuEntity);//保存
        return MenusConverter.INSTANCE.toResponse(menuEntity);
    }

    @Override
    public MenusResponse updateMenus(Long menuId, MenusUpdateRequest request) {
        MenuEntity oldMenuEntity = getOptById(menuId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MENU_NOT_FOUND));
        //新的值复制到旧的值
        MenusConverter.INSTANCE.copy(request, oldMenuEntity);
        checkNameUnique(oldMenuEntity); // 检查名称唯一性

        MenuEntity menuEntity = MenusConverter.INSTANCE.toEntity(request);
        menuEntity.setMenuId(menuId);
        updateById(menuEntity);//会跳过null字段

        return MenusConverter.INSTANCE.toResponse(oldMenuEntity);
    }

    @Override
    public List<MenusResponse> listMenus(String storeId) {

        return MenusConverter.INSTANCE.toResponse(
                lambdaQuery()
                        .eq(MenuEntity::getStoreId, storeId)
                        .list()
        );
    }
}
