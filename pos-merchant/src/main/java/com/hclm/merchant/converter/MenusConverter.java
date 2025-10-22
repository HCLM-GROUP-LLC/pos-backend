package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.MenusAddRequest;
import com.hclm.merchant.pojo.request.MenusUpdateRequest;
import com.hclm.merchant.pojo.response.MenusResponse;
import com.hclm.web.entity.Menus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenusConverter {
    MenusConverter INSTANCE = Mappers.getMapper(MenusConverter.class);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link Menus }
     */
    Menus toEntity(MenusAddRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link Menus }
     */
    Menus toEntity(MenusUpdateRequest request);

    void copy(MenusUpdateRequest request, @MappingTarget Menus entity);

    /**
     * 响应
     *
     * @param entity 实体
     * @return {@link MenusResponse }
     */
    MenusResponse toResponse(Menus entity);

    /**
     * 响应
     *
     * @param entity 菜单
     * @return {@link MenusResponse }
     */
    List<MenusResponse> toResponse(List<Menus> entity);
}
