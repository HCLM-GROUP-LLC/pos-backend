package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.MenuItemAddRequest;
import com.hclm.merchant.pojo.request.MenuItemUpdateRequest;
import com.hclm.merchant.pojo.response.MenuItemResponse;
import com.hclm.mybatis.entity.MenuItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuItemConverter {
    MenuItemConverter INSTANCE = Mappers.getMapper(MenuItemConverter.class);

    /**
     * 响应
     *
     * @param menuItem 菜单项
     * @return {@link MenuItemResponse }
     */
    MenuItemResponse toResponse(MenuItemEntity menuItem);

    /**
     * 响应
     *
     * @param menuItems 菜单项
     * @return {@link List }<{@link MenuItemResponse }>
     */
    List<MenuItemResponse> toResponse(List<MenuItemEntity> menuItems);

    MenuItemEntity toEntity(MenuItemAddRequest request);

    MenuItemEntity toEntity(MenuItemUpdateRequest request);
}
