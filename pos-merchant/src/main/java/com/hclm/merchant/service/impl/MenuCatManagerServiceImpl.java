package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.MenuCatConverter;
import com.hclm.merchant.pojo.request.MenuCatAddRequest;
import com.hclm.merchant.pojo.request.MenuCatUpdateRequest;
import com.hclm.merchant.pojo.response.MenuCatResponse;
import com.hclm.merchant.service.MenuCatManagerService;
import com.hclm.merchant.service.MenusManagerService;
import com.hclm.mybatis.entity.MenuCatEntity;
import com.hclm.mybatis.mapper.MenuCatMapper;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单类别经理服务实现
 *
 * @author hanhua
 * @since 2025/10/22
 */
@RequiredArgsConstructor
@Service
public class MenuCatManagerServiceImpl extends ServiceImpl<MenuCatMapper, MenuCatEntity> implements MenuCatManagerService {
    private final MenusManagerService menuManagerServce;

    private void checkNameUnique(MenuCatEntity categories) {
        if (lambdaQuery()
                .eq(MenuCatEntity::getMenuId, categories.getMenuId())
                .eq(MenuCatEntity::getCategoryName, categories.getCategoryName())
                .ne(categories.getCategoryId() != null, MenuCatEntity::getCategoryId, categories.getCategoryId()) // 排除自身
                .exists()) {
            throw new BusinessException(ResponseCode.MENU_CATEGORY_NAME_ALREADY_EXISTS);
        }
    }

    @Override
    public MenuCatResponse add(MenuCatAddRequest request) {
        var menu = menuManagerServce.getOptById(request.getMenuId())
                .orElseThrow(() -> new BusinessException(ResponseCode.MENU_NOT_FOUND));// 检查菜单id

        MenuCatEntity categories = MenuCatConverter.INSTANCE.toEntity(request);
        categories.setStoreId(menu.getStoreId());// 设置门店id,跟菜单一致
        checkNameUnique(categories);// 检查名称唯一性
        save(categories);//保存
        return MenuCatConverter.INSTANCE.toResponse(categories);
    }

    @Override
    public MenuCatResponse update(Long categoryId, MenuCatUpdateRequest request) {
        MenuCatEntity oldCategory = getOptById(categoryId)
                .orElseThrow(() -> new BusinessException(ResponseCode.MENU_CATEGORY_NOT_FOUND));
        //新的值复制到旧的值
        MenuCatConverter.INSTANCE.copy(request, oldCategory);
        checkNameUnique(oldCategory); // 检查名称唯一性

        MenuCatEntity category = MenuCatConverter.INSTANCE.toEntity(request);
        category.setCategoryId(categoryId);
        updateById(category);//会跳过null字段

        return MenuCatConverter.INSTANCE.toResponse(oldCategory);
    }

    @Override
    public List<MenuCatResponse> list(String menuId) {
        return MenuCatConverter.INSTANCE.toResponse(
                lambdaQuery()
                        .eq(MenuCatEntity::getMenuId, menuId)
                        .list()
        );
    }
}
