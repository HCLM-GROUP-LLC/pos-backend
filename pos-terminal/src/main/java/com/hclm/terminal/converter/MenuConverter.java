package com.hclm.terminal.converter;

import com.hclm.mybatis.entity.MenuCatEntity;
import com.hclm.mybatis.entity.MenuEntity;
import com.hclm.mybatis.entity.MenuItemEntity;
import com.hclm.terminal.pojo.response.MenuCatResponse;
import com.hclm.terminal.pojo.response.MenuItemResponse;
import com.hclm.terminal.pojo.response.MenuResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuConverter {
    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    /**
     * 响应
     *
     * @param menuEntity 菜单实体
     * @return {@link MenuResponse }
     */
    MenuResponse toResponse(MenuEntity menuEntity);

    /**
     * 响应
     *
     * @param menuEntity 菜单实体
     * @return {@link List }<{@link MenuResponse }>
     */
    List<MenuResponse> toResponse(List<MenuEntity> menuEntity);

    /**
     * 菜单分类实体转为响应
     *
     * @param menuCatEntity 菜单cat实体
     * @return {@link MenuCatResponse }
     */
    MenuCatResponse toCatResponse(MenuCatEntity menuCatEntity);

    /**
     * 菜单分类实体转为响应
     *
     * @param menuCatEntity 菜单cat实体
     * @return {@link List }<{@link MenuCatResponse }>
     */
    List<MenuCatResponse> toCatResponse(List<MenuCatEntity> menuCatEntity);

    /**
     * 对菜单项响应
     *
     * @param menuItemEntity 菜单项实体
     * @return {@link MenuItemResponse }
     */
    MenuItemResponse toMenuItemResponse(MenuItemEntity menuItemEntity);

    /**
     * 对菜单项响应
     *
     * @param menuItemEntity 菜单项实体
     * @return {@link List }<{@link MenuItemResponse }>
     */
    List<MenuItemResponse> toMenuItemResponse(List<MenuItemEntity> menuItemEntity);
}
