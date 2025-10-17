package com.hclm.merchant.mapper;

import com.hclm.web.entity.MenuCategoryItem;
import com.hclm.merchant.pojo.request.MenuCategoryItemRequest;
import com.hclm.merchant.pojo.response.MenuCategoryItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuCategoryItemMapper {
    MenuCategoryItemMapper INSTANCE = Mappers.getMapper(MenuCategoryItemMapper.class);

    MenuCategoryItem toEntity(MenuCategoryItemRequest request);

    MenuCategoryItemResponse toResponse(MenuCategoryItem entity);
}