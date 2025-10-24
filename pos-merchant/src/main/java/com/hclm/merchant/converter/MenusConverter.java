package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.MenusAddRequest;
import com.hclm.merchant.pojo.request.MenusUpdateRequest;
import com.hclm.merchant.pojo.response.MenusResponse;
import com.hclm.mybatis.entity.MenuEntity;
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
     * @return {@link MenuEntity }
     */
    MenuEntity toEntity(MenusAddRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link MenuEntity }
     */
    MenuEntity toEntity(MenusUpdateRequest request);

    void copy(MenusUpdateRequest request, @MappingTarget MenuEntity entity);

    /**
     * 响应
     *
     * @param entity 实体
     * @return {@link MenusResponse }
     */
    MenusResponse toResponse(MenuEntity entity);

    /**
     * 响应
     *
     * @param entity 菜单
     * @return {@link MenusResponse }
     */
    List<MenusResponse> toResponse(List<MenuEntity> entity);
}
