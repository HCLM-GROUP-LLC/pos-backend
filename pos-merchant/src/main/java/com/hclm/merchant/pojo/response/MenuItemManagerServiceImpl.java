package com.hclm.merchant.pojo.response;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.MenuItemConverter;
import com.hclm.merchant.pojo.request.MenuItemAddRequest;
import com.hclm.merchant.pojo.request.MenuItemUpdateRequest;
import com.hclm.merchant.service.MenuItemManagerService;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.MenuItems;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.mapper.MenuItemsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemManagerServiceImpl extends ServiceImpl<MenuItemsMapper, MenuItems> implements MenuItemManagerService {
    private void checkNameUnique(MenuItems menuItems) {
        if (lambdaQuery()
                .eq(MenuItems::getStoreId, menuItems.getStoreId())
                .eq(MenuItems::getItemName, menuItems.getItemName())
                .ne(menuItems.getItemId() != null, MenuItems::getItemId, menuItems.getItemId()) // 排除自身
                .exists()
        ) {
            throw new BusinessException(ResponseCode.MENU_ITEM_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * 查找由门店id
     *
     * @param storeId 门店id
     * @return {@link List }<{@link MenuItemResponse }>
     */
    @Override
    public List<MenuItemResponse> findByStoreId(String storeId) {
        return MenuItemConverter.INSTANCE.toResponse(
                lambdaQuery()
                        .eq(MenuItems::getStoreId, storeId)
                        .list()
        );
    }

    /**
     * 按类别id查找
     *
     * @param categoryId 类别id
     * @return {@link List }<{@link MenuItemResponse }>
     */
    @Override
    public List<MenuItemResponse> findByCategoryId(Long categoryId) {
        return MenuItemConverter.INSTANCE.toResponse(
                getBaseMapper().findByCategoryId(categoryId)
        );
    }

    @Override
    public MenuItemResponse create(MenuItemAddRequest request) {
        MenuItems menuItems = MenuItemConverter.INSTANCE.toEntity(request);
        checkNameUnique(menuItems);
        save(menuItems);
        return MenuItemConverter.INSTANCE.toResponse(menuItems);
    }

    @Override
    public MenuItemResponse update(Long menuItemId, MenuItemUpdateRequest request) {
        if (request.isEmpty()) {// 没有修改
            return MenuItemConverter.INSTANCE.toResponse(getById(menuItemId));
        }
        MenuItems menuItems = MenuItemConverter.INSTANCE.toEntity(request);
        menuItems.setItemId(menuItemId);
        checkNameUnique(menuItems);
        updateById(menuItems);
        return MenuItemConverter.INSTANCE.toResponse(menuItems);
    }
}
