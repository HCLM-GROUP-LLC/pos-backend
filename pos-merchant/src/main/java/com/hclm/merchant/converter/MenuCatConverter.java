package com.hclm.merchant.converter;

import com.hclm.merchant.pojo.request.MenuCatAddRequest;
import com.hclm.merchant.pojo.request.MenuCatUpdateRequest;
import com.hclm.merchant.pojo.response.MenuCatResponse;
import com.hclm.mybatis.entity.MenuCategorieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuCatConverter {
    MenuCatConverter INSTANCE = Mappers.getMapper(MenuCatConverter.class);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link MenuCategorieEntity }
     */
    MenuCategorieEntity toEntity(MenuCatAddRequest request);

    /**
     * 至实体
     *
     * @param request 请求
     * @return {@link MenuCategorieEntity }
     */
    MenuCategorieEntity toEntity(MenuCatUpdateRequest request);

    void copy(MenuCatUpdateRequest request, @MappingTarget MenuCategorieEntity entity);

    /**
     * 响应
     *
     * @param entity 实体
     * @return {@link MenuCatResponse }
     */
    MenuCatResponse toResponse(MenuCategorieEntity entity);

    /**
     * 响应
     *
     * @param entity 菜单
     * @return {@link MenuCatResponse }
     */
    List<MenuCatResponse> toResponse(List<MenuCategorieEntity> entity);
}
