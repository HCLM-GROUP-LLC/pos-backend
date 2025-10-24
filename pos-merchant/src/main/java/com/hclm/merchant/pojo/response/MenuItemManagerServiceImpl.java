package com.hclm.merchant.pojo.response;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.MenuItemConverter;
import com.hclm.merchant.pojo.request.MenuItemAddRequest;
import com.hclm.merchant.pojo.request.MenuItemUpdateRequest;
import com.hclm.merchant.service.MenuItemManagerService;
import com.hclm.mybatis.entity.MenuItemEntity;
import com.hclm.mybatis.mapper.MenuItemMapper;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemManagerServiceImpl extends ServiceImpl<MenuItemMapper, MenuItemEntity> implements MenuItemManagerService {
    private void checkNameUnique(MenuItemEntity menuItemEntity) {
        if (lambdaQuery()
                .eq(MenuItemEntity::getStoreId, menuItemEntity.getStoreId())
                .eq(MenuItemEntity::getItemName, menuItemEntity.getItemName())
                .ne(menuItemEntity.getItemId() != null, MenuItemEntity::getItemId, menuItemEntity.getItemId()) // 排除自身
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
                        .eq(MenuItemEntity::getStoreId, storeId)
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
        MenuItemEntity menuItemEntity = MenuItemConverter.INSTANCE.toEntity(request);
        checkNameUnique(menuItemEntity);
        save(menuItemEntity);
        return MenuItemConverter.INSTANCE.toResponse(menuItemEntity);
    }

    @Override
    public MenuItemResponse update(Long menuItemId, MenuItemUpdateRequest request) {
        if (request.isEmpty()) {// 没有修改
            return MenuItemConverter.INSTANCE.toResponse(getById(menuItemId));
        }
        MenuItemEntity menuItemEntity = MenuItemConverter.INSTANCE.toEntity(request);
        menuItemEntity.setItemId(menuItemId);
        checkNameUnique(menuItemEntity);
        updateById(menuItemEntity);
        return MenuItemConverter.INSTANCE.toResponse(menuItemEntity);
    }
}
